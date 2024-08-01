package com.avyay.e_commerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avyay.e_commerce.entity.Category;
import com.avyay.e_commerce.entity.DataObject;
import com.avyay.e_commerce.entity.Product;
import com.avyay.e_commerce.repository.CategoryRepository;
import com.avyay.e_commerce.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class DataService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void populateData(DataObject dataObject) {
        Category category = categoryRepository.findByName(dataObject.getCategoryName())
                .orElseGet(() -> createCategory(dataObject.getCategoryName()));

        for (DataObject.ProductData productData : dataObject.getProducts()) {
            Product product = convertToEntity(productData, category);
            productRepository.save(product);
        }
    }

    private Category createCategory(String categoryName) {
        Category category = new Category();
        category.setName(categoryName);
        return categoryRepository.save(category);
    }

    private Product convertToEntity(DataObject.ProductData productData, Category category) {
        return Product.builder()
                .name(productData.getName())
                .price(productData.getPrice())
                .description(productData.getDescription())
                .category(category)
                .build();
    }

}
