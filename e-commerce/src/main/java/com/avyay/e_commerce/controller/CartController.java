package com.avyay.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avyay.e_commerce.dto.CartDTO;
import com.avyay.e_commerce.dto.CartItemDTO;
import com.avyay.e_commerce.exception.UnauthorizedException;
import com.avyay.e_commerce.feign.UserServiceClient;
import com.avyay.e_commerce.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserServiceClient userServiceClient;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CartDTO createCart(@RequestBody CartDTO cartDTO, Authentication authentication) {
        String currentUsername = authentication.getName();

        String authenticatedUsername = userServiceClient.findUsernameById(cartDTO.getUserId()).getBody();
        if (!currentUsername.equals(authenticatedUsername)) {
            throw new UnauthorizedException("You are not authorized to perform this action");
        }

        return cartService.createCart(cartDTO);
    }

    @GetMapping("/get/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public CartDTO getCartById(@PathVariable Long userId, Authentication authentication) {
        String currentUsername = authentication.getName();

        String authenticatedUsername = userServiceClient.findUsernameById(userId).getBody();
        if (!currentUsername.equals(authenticatedUsername)) {
            throw new UnauthorizedException("You are not authorized to perform this action");
        }
        return cartService.getCartById(userId);
    }

    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public CartDTO updateCart(@PathVariable Long userId, @RequestBody CartItemDTO cartItemDTO,
            Authentication authentication) {
        String currentUsername = authentication.getName();

        String authenticatedUsername = userServiceClient.findUsernameById(userId).getBody();

        if (!currentUsername.equals(authenticatedUsername)) {
            throw new UnauthorizedException("You are not authorized to perform this action");
        }

        return cartService.updateCart(userId, cartItemDTO);
    }

    @DeleteMapping("/delete/{cartItemId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCart(@PathVariable Long cartItemId) {
        cartService.deleteCart(cartItemId);
    }
}
