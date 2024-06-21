package com.avyay.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avyay.e_commerce.entity.Category;
import com.avyay.e_commerce.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

    List<Product> findByNameContainingIgnoreCase(String pattern);
}
