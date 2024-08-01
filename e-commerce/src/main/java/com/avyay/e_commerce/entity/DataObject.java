package com.avyay.e_commerce.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataObject {

    private String categoryName;
    private List<ProductData> products;

    @Data
    public static class ProductData {
        private String name;
        private double price;
        private String description;
    }
}
