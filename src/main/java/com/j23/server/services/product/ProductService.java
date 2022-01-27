package com.j23.server.services.product;

import com.j23.server.models.product.Product;
import com.j23.server.repos.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Page<Product> findAllProduct(Pageable pageable, Long minCalories, Long maxCalories,
                                        BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findAllByTotalCaloriesBetweenAndUnitPriceBetween(minCalories,
                maxCalories, minPrice, maxPrice, pageable);
    }

    public Page<Product> findAllProductByName(String name, Pageable pageable) {
        return productRepository.findAllByNameContaining(name, pageable);
    }

    public Page<Product> findAllProductByNameAndFilter(String name, Pageable pageable, Long minCalories, Long maxCalories,
                                              BigDecimal minPrice, BigDecimal maxPrice
    ) {
        return productRepository.findAllByNameContainingAndTotalCaloriesBetweenAndUnitPriceBetween(name, minCalories,
                maxCalories, minPrice, maxPrice, pageable);
    }


    public Page<Product> findAllProductByFilter(Long id, Pageable pageable, Long minCalories, Long maxCalories,
                                                BigDecimal minPrice, BigDecimal maxPrice
    ) {
        return productRepository.findAllByCategoryIdAndTotalCaloriesBetweenAndUnitPriceBetween(id, minCalories,
                maxCalories, minPrice, maxPrice, pageable);
    }

}
