package com.avyay.e_commerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avyay.e_commerce.dto.CartDTO;
import com.avyay.e_commerce.dto.CartItemDTO;
import com.avyay.e_commerce.entity.Cart;
import com.avyay.e_commerce.entity.CartItem;
import com.avyay.e_commerce.entity.Product;
import com.avyay.e_commerce.exception.ProductNotFoundException;
import com.avyay.e_commerce.exception.ResourceNotFoundException;
import com.avyay.e_commerce.repository.CartItemRepository;
import com.avyay.e_commerce.repository.CartRepository;
import com.avyay.e_commerce.repository.ProductRepository;
import com.avyay.e_commerce.repository.UserRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    public CartDTO createCart(CartDTO cartDTO) {
        Cart cart = new Cart();
        cart.setUserId(cartDTO.getUserId());

        List<CartItem> cartItems = cartDTO.getItems().stream()
                .map(itemDTO -> {
                    CartItem cartItem = new CartItem();
                    cartItem.setProduct(productRepository.findById(itemDTO.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException("Product not found")));
                    cartItem.setQuantity(itemDTO.getQuantity());
                    cartItem.setCart(cart);
                    cartItem.calculatePrice();
                    return cartItem;
                })
                .collect(Collectors.toList());

        cart.setItems(cartItems);
        cart.calculateTotalPrice();

        Cart savedCart = cartRepository.save(cart);

        return convertCartToDTO(savedCart);
    }

    public CartDTO getCartById(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));

        return convertCartToDTO(cart);
    }

    public CartDTO updateCart(Long cartItemId, CartItemDTO cartItemDTO) {
        CartItem existingCartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart-Item Not found"));

        existingCartItem.setQuantity(cartItemDTO.getQuantity());
        existingCartItem.calculatePrice();

        Cart cart = existingCartItem.getCart();
        cart.calculateTotalPrice();

        cartItemRepository.save(existingCartItem);
        cartRepository.save(cart);

        return convertCartToDTO(cart);
    }

    public void deleteCart(Long cartItemId) {
        CartItem existingCartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart-Item Not found"));

        Cart cart = existingCartItem.getCart();
        cart.getItems().remove(existingCartItem);
        cartItemRepository.delete(existingCartItem);

        cart.calculateTotalPrice();

        cartRepository.save(cart);
    }

    // Helper function

    private CartItem convertCartItemDTOToEntity(CartItemDTO cartItemDTO, Cart cart) {
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product not found with id " + cartItemDTO.getProductId()));

        return CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(cartItemDTO.getQuantity())
                .build();
    }

    private CartDTO convertCartToDTO(Cart cart) {

        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(this::convertCartItemToDTO)
                .collect(Collectors.toList());

        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .totalPrice(cart.getTotalPrice())
                .items(itemDTOs).build();
    }

    private CartItemDTO convertCartItemToDTO(CartItem cartItem) {
        return CartItemDTO.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .build();
    }
}
