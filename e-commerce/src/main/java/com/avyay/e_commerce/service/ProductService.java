package com.avyay.e_commerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avyay.e_commerce.dto.ProductDTO;
import com.avyay.e_commerce.entity.Category;
import com.avyay.e_commerce.entity.Product;
import com.avyay.e_commerce.exception.CategoryNotFoundException;
import com.avyay.e_commerce.exception.ProductNotFoundException;
import com.avyay.e_commerce.exception.ResourceNotFoundException;
import com.avyay.e_commerce.repository.CategoryRepository;
import com.avyay.e_commerce.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertProductToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

        return convertProductToDTO(product);
    }

    public List<ProductDTO> getProductByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category Not Found"));

        return productRepository.findByCategory(category).stream()
                .map(this::convertProductToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategoryName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));

        return productRepository.findByCategory(category).stream()
                .map(this::convertProductToDTO)
                .collect(Collectors.toList());
    }

    public void createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .build();

        Category category = categoryRepository.findByName(productDTO.getCategory())
                .map(existingCategory -> existingCategory)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(productDTO.getCategory());
                    return categoryRepository.save(newCategory);
                });

        // category.getProducts().add(product);
        // categoryRepository.save(category);

        product.setCategory(category);

        productRepository.save(product);
    }

    public void updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());

        Category category = categoryRepository.findByName(productDTO.getCategory())
                .map(existingCategory -> existingCategory)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(productDTO.getCategory());
                    return categoryRepository.save(newCategory);
                });

        // category.getProducts().add(existingProduct);
        // categoryRepository.save(category);

        existingProduct.setCategory(category);

        productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDTO convertProductToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .category(product.getCategory().getName())
                .build();

    }

}
