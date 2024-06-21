package com.avyay.e_commerce.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartDTO {
    private Long id;
    private Long userId;
    private double totalPrice;
    @Builder.Default
    private List<CartItemDTO> items = new ArrayList<>();
}
