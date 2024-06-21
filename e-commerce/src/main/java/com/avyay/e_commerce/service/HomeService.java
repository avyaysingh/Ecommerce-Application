package com.avyay.e_commerce.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avyay.e_commerce.dto.ProductDTO;
import com.avyay.e_commerce.entity.Product;
import com.avyay.e_commerce.repository.ProductRepository;

@Service
public class HomeService {

    @Autowired
    private ProductRepository productRepository;

    public Map<String, List<ProductDTO>> getAllProductsByCategory() {
        List<Product> products = productRepository.findAll();

        return categorizeProducts(products);
    }

    public Map<String, List<ProductDTO>> searchProducts(String pattern) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(pattern);
        return categorizeProducts(products);
    }

    private Map<String, List<ProductDTO>> categorizeProducts(List<Product> products) {
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.groupingBy(
                        productDTO -> productDTO.getCategory(), Collectors.toList()));
    }

    private ProductDTO convertToDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .category(product.getCategory().getName())
                .build();
    }

}
