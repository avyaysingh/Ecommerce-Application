package com.avyay.e_commerce.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    private String description;
    // private Long categoryId;
    private String category;
}
