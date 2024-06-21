package com.avyay.e_commerce.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avyay.e_commerce.dto.AddressDTO;
import com.avyay.e_commerce.dto.OrderDTO;
import com.avyay.e_commerce.dto.OrderItemDTO;
import com.avyay.e_commerce.dto.OrderItemSummaryDTO;
import com.avyay.e_commerce.dto.OrderSummaryDTO;
import com.avyay.e_commerce.entity.Address;
import com.avyay.e_commerce.entity.Order;
import com.avyay.e_commerce.entity.OrderItem;
import com.avyay.e_commerce.entity.Product;
import com.avyay.e_commerce.exception.ResourceNotFoundException;
import com.avyay.e_commerce.repository.OrderRepository;
import com.avyay.e_commerce.repository.ProductRepository;

@Service
public class OrderService {

        @Autowired
        private OrderRepository orderRepository;

        @Autowired
        private ProductRepository productRepository;

        public OrderDTO placeOrder(OrderDTO orderDTO) {
                Order order = convertToEntity(orderDTO);
                order.calculateTotalPrice();

                Order savedOrder = orderRepository.save(order);
                return convertToDto(savedOrder);
        }

        public List<OrderDTO> getAllOrders() {
                return orderRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
        }

        public OrderDTO getOrderById(Long id) {
                Order order = orderRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found for given Id"));

                return convertToDto(order);
        }

        public OrderSummaryDTO getOrderSummary(Long id) {
                Order order = orderRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found for given id"));

                Set<OrderItemSummaryDTO> orderItems = order.getOrderItems().stream()
                                .map(this::convertToOrderItemSummaryDTO)
                                .collect(Collectors.toSet());

                return OrderSummaryDTO.builder()
                                .orderId(order.getId())
                                .userId(order.getUserId())
                                .totalPrice(order.getTotalPrice())
                                .shippingAddress(convertToDto(order.getShippingAddress()))
                                .orderItems(orderItems)
                                .build();
        }

        private OrderItemSummaryDTO convertToOrderItemSummaryDTO(OrderItem orderItem) {
                return OrderItemSummaryDTO.builder()
                                .productId(orderItem.getProduct().getId())
                                .productName(orderItem.getProduct().getName())
                                .quantity(orderItem.getQuantity())
                                .price(orderItem.getPrice())
                                .build();
        }

        private Order convertToEntity(OrderDTO orderDTO) {
                Set<OrderItem> orderItems = orderDTO.getOrderItems().stream()
                                .map(orderItemDTO -> {
                                        OrderItem orderItem = convertOrderItemDTOToEntity(orderItemDTO);
                                        orderItem.setOrder(null); // Break the cyclic reference
                                        return orderItem;
                                })
                                .collect(Collectors.toSet());

                Order order = Order.builder()
                                .userId(orderDTO.getUserId())
                                .shippingAddress(convertToEntity(orderDTO.getShippingAddress()))
                                .orderItems(orderItems)
                                .build();

                orderItems.forEach(orderItem -> orderItem.setOrder(order)); // Set the owning side

                return order;
        }

        private OrderItem convertOrderItemDTOToEntity(OrderItemDTO orderItemDTO) {
                Product product = productRepository.findById(orderItemDTO.getProductId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Product not found with id " + orderItemDTO.getProductId()));

                OrderItem orderItem = OrderItem.builder()
                                .product(product)
                                .quantity(orderItemDTO.getQuantity())
                                .price(orderItemDTO.getQuantity() * product.getPrice())
                                .build();

                return orderItem;
        }

        private OrderDTO convertToDto(Order order) {
                Set<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                                .map(this::convertOrderItemToDTO)
                                .collect(Collectors.toSet());

                return OrderDTO.builder()
                                .id(order.getId())
                                .userId(order.getUserId())
                                .totalPrice(order.getTotalPrice())
                                .shippingAddress(convertToDto(order.getShippingAddress()))
                                .orderItems(orderItemDTOs)
                                .build();
        }

        private OrderItemDTO convertOrderItemToDTO(OrderItem orderItem) {
                return OrderItemDTO.builder()
                                .id(orderItem.getId())
                                .productId(orderItem.getProduct().getId())
                                .quantity(orderItem.getQuantity())
                                .price(orderItem.getPrice())
                                .build();
        }

        private Address convertToEntity(AddressDTO addressDTO) {
                return Address.builder()
                                .city(addressDTO.getCity())
                                .street(addressDTO.getStreet())
                                .firstName(addressDTO.getFirstName())
                                .lastName(addressDTO.getLastName())
                                .build();
        }

        private AddressDTO convertToDto(Address address) {
                return AddressDTO.builder()
                                .city(address.getCity())
                                .street(address.getStreet())
                                .firstName(address.getFirstName())
                                .lastName(address.getLastName())
                                .build();
        }
}
