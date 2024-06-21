package com.avyay.e_commerce.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private int quantity;
    private double price;
}
