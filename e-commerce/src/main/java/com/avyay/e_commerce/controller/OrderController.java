package com.avyay.e_commerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avyay.e_commerce.dto.AddressDTO;
import com.avyay.e_commerce.dto.OrderDTO;
import com.avyay.e_commerce.dto.OrderItemDTO;
import com.avyay.e_commerce.exception.UnauthorizedException;
import com.avyay.e_commerce.feign.UserServiceClient;
import com.avyay.e_commerce.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserServiceClient userServiceClient;

    @PostMapping("/{userId}/checkout")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO placeOrder(@PathVariable Long userId, @RequestBody AddressDTO addressDTO,
            Authentication authentication) {
        // String currentUsername = authentication.getName();

        // String authenticatedUsername =
        // userServiceClient.findUsernameById(userId).getBody();
        if (!isUserAuthenticated(userId, authentication)) {
            throw new UnauthorizedException("You are not authorized to perform this action");
        }

        return orderService.placeOrder(userId, addressDTO);
    }

    private boolean isUserAuthenticated(Long userId, Authentication authentication) {
        return authentication.getName().equals(userServiceClient.findUsernameById(userId).getBody());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemDTO> viewAllOrders(@PathVariable Long userId, Authentication authentication) {
        // String currentUsername = authentication.getName();

        // String authenticatedUsername =
        // userServiceClient.findUsernameById(userId).getBody();
        // if (!currentUsername.equals(authenticatedUsername)) {
        // throw new UnauthorizedException("You are not authorized to perform this
        // action");
        // }

        if (!isUserAuthenticated(userId, authentication)) {
            throw new UnauthorizedException("You are not authorized to perform this action");
        }

        return orderService.viewAllOrders(userId);
    }

    @DeleteMapping("/{userId}/cancel/{orderItemId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public OrderItemDTO cancelOrder(@PathVariable Long userId, @PathVariable Long orderItemId,
            Authentication authentication) {
        // String currentUsername = authentication.getName();

        // String authenticatedUsername =
        // userRepository.findUsernameById(userId).get().getUsername();
        if (!isUserAuthenticated(userId, authentication)) {
            throw new UnauthorizedException("You are not authorized to perform this action");
        }

        return orderService.cancelOrder(userId, orderItemId);
    }
}
