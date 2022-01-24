package com.j23.server.repos.product;

import com.j23.server.models.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    Page<Product> findByCategoryIdAndTotalCaloriesBetweenAndUnitPriceBetween(Long id, Long minCalories,
                                                                             Long maxCalories, BigDecimal minPrice,
                                                                             BigDecimal maxPrice,
                                                                             Pageable pageable);

}
