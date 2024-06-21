package com.avyay.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.avyay.e_commerce.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CartDTO createCart(@RequestBody CartDTO cartDTO) {
        return cartService.createCart(cartDTO);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public CartDTO getCartById(@PathVariable Long id) {
        return cartService.getCartById(id);
    }

    @PutMapping("/update/{cartItemId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public CartDTO updateCart(@PathVariable Long cartItemId, @RequestBody CartItemDTO cartItemDTO) {
        return cartService.updateCart(cartItemId, cartItemDTO);
    }

    @DeleteMapping("/delete/{cartItemId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCart(@PathVariable Long cartItemId) {
        cartService.deleteCart(cartItemId);
    }
}
