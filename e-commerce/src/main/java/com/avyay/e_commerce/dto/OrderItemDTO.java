package com.avyay.e_commerce.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderItemDTO {
    private Date orderDate;
    private String productName;
    private Double price;
    private Integer quantity;
    private Double totalAmount;
    private String shippingAddress;
}
