package com.avyay.e_commerce.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDTO {
    private Long id;
    private Long userId;
    private double totalPrice;
    private AddressDTO shippingAddress;
    @Builder.Default
    private Set<OrderItemDTO> orderItems = new HashSet<>();
}
