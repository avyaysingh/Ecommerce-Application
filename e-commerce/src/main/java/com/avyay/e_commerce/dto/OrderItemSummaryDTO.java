package com.avyay.e_commerce.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemSummaryDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private double price;
}
