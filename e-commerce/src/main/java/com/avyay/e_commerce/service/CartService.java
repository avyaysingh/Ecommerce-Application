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

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartDTO createCart(CartDTO cartDTO) {
        Cart cart = cartRepository.findByUserId(cartDTO.getUserId()).orElse(new Cart());
        cart.setUserId(cartDTO.getUserId());

        List<CartItem> cartItems = cartDTO.getItems().stream()
                .map(itemDTO -> {
                    CartItem cartItem = new CartItem();
                    Product product = productRepository.findById(itemDTO.getProductId())
                            .orElseThrow(() -> new ProductNotFoundException("Product not found"));
                    cartItem.setProduct(product);
                    cartItem.setProductName(product.getName());
                    cartItem.setQuantity(itemDTO.getQuantity());
                    cartItem.setCart(cart);
                    cartItem.calculatePrice();
                    return cartItem;
                })
                .collect(Collectors.toList());

        if (cart.getId() != null) {
            List<CartItem> existingItems = cart.getItems();
            for (CartItem newItem : cartItems) {
                boolean found = false;
                for (CartItem existingItem : existingItems) {
                    if (existingItem.getProduct().getId().equals(newItem.getProduct().getId())) {
                        existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                        existingItem.calculatePrice();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    existingItems.add(newItem);
                }
            }
        } else {
            cart.setItems(cartItems);
        }

        cart.calculateTotalPrice();

        Cart savedCart = cartRepository.save(cart);

        return convertCartToDTO(savedCart);
    }

    public CartDTO getCartById(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));

        return convertCartToDTO(cart);
    }

    public CartDTO updateCart(Long userId, CartItemDTO cartItemDTO) {
        Cart existingCart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Not found for user"));

        List<CartItem> cartItems = existingCart.getItems();

        boolean isItemUpdated = false;

        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(cartItemDTO.getProductId())) {
                item.setQuantity(cartItemDTO.getQuantity());
                item.calculatePrice();
                isItemUpdated = true;
                break;
            }
        }

        if (!isItemUpdated) {
            CartItem newItem = new CartItem();
            Product product = productRepository.findById(cartItemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
            newItem.setProduct(product);
            newItem.setProductName(product.getName());
            newItem.setQuantity(cartItemDTO.getQuantity());
            newItem.setCart(existingCart);
            newItem.calculatePrice();
            cartItems.add(newItem);
        }

        existingCart.calculateTotalPrice();
        Cart savedCart = cartRepository.save(existingCart);

        return convertCartToDTO(savedCart);
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
                .productName(cartItem.getProductName())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .build();
    }
}
