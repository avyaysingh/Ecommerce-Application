package com.avyay.e_commerce.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avyay.e_commerce.dto.ProductDTO;
import com.avyay.e_commerce.service.HomeService;

import jakarta.annotation.security.PermitAll;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping
    @PermitAll
    public Map<String, List<ProductDTO>> getAllProductsByCategory() {
        return homeService.getAllProductsByCategory();
    }

    @GetMapping("/search")
    @PermitAll
    public Map<String, List<ProductDTO>> searchProducts(@RequestParam String pattern) {
        return homeService.searchProducts(pattern);
    }
}
