package com.j23.server.services.product;

import com.j23.server.models.product.Product;
import com.j23.server.repos.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Page<Product> findAllProduct(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> findAllProductByFilter(Long id, Pageable pageable, Long minCalories, Long maxCalories) {
        return productRepository.findByCategoryIdAndTotalCaloriesBetween(id, minCalories, maxCalories, pageable );
    }

}
