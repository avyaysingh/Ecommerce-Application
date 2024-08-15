package com.avyay.e_commerce.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.avyay.e_commerce.dto.AddressDTO;
import com.avyay.e_commerce.dto.OrderDTO;
import com.avyay.e_commerce.dto.OrderItemDTO;
import com.avyay.e_commerce.entity.Address;
import com.avyay.e_commerce.entity.Cart;
import com.avyay.e_commerce.entity.CartItem;
import com.avyay.e_commerce.entity.Order;
import com.avyay.e_commerce.entity.OrderItem;
import com.avyay.e_commerce.exception.ResourceNotFoundException;
import com.avyay.e_commerce.exception.UnauthorizedException;
import com.avyay.e_commerce.repository.CartRepository;
import com.avyay.e_commerce.repository.OrderItemRepository;
import com.avyay.e_commerce.repository.OrderRepository;
import com.avyay.e_commerce.repository.UserRepository;

@Service
public class OrderService {

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private CartRepository cartRepository;

        @Autowired
        private OrderItemRepository orderItemRepository;

        @Autowired
        private UserRepository userRepository;

        public OrderDTO placeOrder(Long userId, AddressDTO addressDTO, Authentication authentication) {

                if (!isUserAuthenticated(userId, authentication)) {
                        throw new UnauthorizedException("You are not authorized to perform this action");
                }

                Cart cart = cartRepository.findByUserId(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));

                Order order = Order.builder().userId(userId).orderItems(new ArrayList<>()).build();
                Address shippingAddress = Address.builder()
                                .name(addressDTO.getName())
                                .city(addressDTO.getCity())
                                .street(addressDTO.getStreet())
                                .state(addressDTO.getState())
                                .postalCode(addressDTO.getPostalCode())
                                .country(addressDTO.getCountry())
                                .phone(addressDTO.getPhone())
                                .build();

                for (CartItem cartItem : cart.getItems()) {
                        OrderItem orderItem = OrderItem.builder()
                                        .order(order)
                                        .product(cartItem.getProduct())
                                        .quantity(cartItem.getQuantity())
                                        // .price(cartItem.getProduct().getPrice())
                                        .orderDate(new Date())
                                        .shippingAddress(shippingAddress)
                                        // .totalAmount(cartItem.getQuantity() * cartItem.getProduct().getPrice())
                                        .build();
                        order.getOrderItems().add(orderItem);

                }

                Order savedOrder = orderRepository.save(order);

                // clearing the cart after placing the order
                cart.getItems().clear();
                cartRepository.save(cart);

                return convertOrderToDTO(savedOrder);

        }

        public OrderItemDTO cancelOrder(Long userId, Long orderItemId, Authentication authentication) {

                if (!isUserAuthenticated(userId, authentication)) {
                        throw new UnauthorizedException("You are not authorized to perform this action");
                }

                OrderItem orderItem = orderItemRepository.findById(orderItemId)
                                .orElseThrow(() -> new ResourceNotFoundException("Order Item Not Found"));

                Order order = orderRepository.findByUserId(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found for provided Id"));
                order.getOrderItems().remove(orderItem);

                orderRepository.save(order);

                return convertOrderItemToDTO(orderItem);
        }

        public List<OrderItemDTO> viewAllOrders(Long userId, Authentication authentication) {

                if (!isUserAuthenticated(userId, authentication)) {
                        throw new UnauthorizedException("You are not authorized to perform this action");
                }

                Order order = orderRepository.findByUserId(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));

                List<OrderItem> orderItems = order.getOrderItems();

                return orderItems.stream().map(this::convertOrderItemToDTO).collect(Collectors.toList());
        }

        // Helper functions

        private boolean isUserAuthenticated(Long userId, Authentication authentication) {
                return authentication.getName().equals(userRepository.findUsernameById(userId).get().getUsername());
        }

        private OrderDTO convertOrderToDTO(Order order) {
                List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
                                .map(this::convertOrderItemToDTO)
                                .collect(Collectors.toList());

                return OrderDTO.builder()
                                .orderId(order.getId())
                                .orderItems(itemDTOs)
                                .build();

        }

        private OrderItemDTO convertOrderItemToDTO(OrderItem orderItem) {
                return OrderItemDTO.builder()
                                .orderDate(orderItem.getOrderDate())
                                .productName(orderItem.getProduct().getName())
                                .price(orderItem.getProduct().getPrice())
                                .quantity(orderItem.getQuantity())
                                .totalAmount(orderItem.getTotalAmount())
                                .shippingAddress(orderItem.getShippingAddress().toString())
                                .build();
        }

}
