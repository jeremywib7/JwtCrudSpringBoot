package com.j23.server.services.product;

import com.j23.server.models.product.Product;
import com.j23.server.repos.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        product.setId(String.valueOf(UUID.randomUUID()));

        LocalDateTime localDateTime = LocalDateTime.now();
        product.setCreatedOn(LocalDateTime.from(localDateTime));

        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        LocalDateTime localDateTime = LocalDateTime.now();
        product.setUpdatedOn(LocalDateTime.from(localDateTime));

        return productRepository.save(product);
    }

    public void deleteProductById(String id) {
        productRepository.deleteProductById(id);
    }


    public Page<Product> findAllProduct(Pageable pageable, Long minCalories, Long maxCalories,
                                        BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findAllByTotalCaloriesBetweenAndUnitPriceBetween(minCalories,
                maxCalories, minPrice, maxPrice, pageable);
    }

    public Page<Product> findAllProductByName(String name, Pageable pageable) {
        return productRepository.findAllByNameContaining(name, pageable);
    }

    public Optional<Product> findProductById(String id) {
        return productRepository.findById(id);
    }

    public Iterable<Product> findAllProductByNameAutoComplete(String name) {
        return productRepository.findAllByNameContaining(name);
    }

    public Page<Product> findAllProductByFilter(String id, Pageable pageable, Long minCalories, Long maxCalories,
                                                BigDecimal minPrice, BigDecimal maxPrice
    ) {
        return productRepository.findAllByCategoryIdAndTotalCaloriesBetweenAndUnitPriceBetween(id, minCalories,
                maxCalories, minPrice, maxPrice, pageable);
    }

}
