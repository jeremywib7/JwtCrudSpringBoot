package com.j23.server.repos.product;

import com.j23.server.models.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(String id);

    Page<Product> findAllByTotalCaloriesBetweenAndUnitPriceBetween(Long minCalories,
                                                                   Long maxCalories, BigDecimal minPrice,
                                                                   BigDecimal maxPrice,
                                                                   Pageable pageable);

    Page<Product> findAllByNameContaining(String name, Pageable pageable);

    Iterable<Product> findAllByNameContaining(String name);

    Page<Product> findAllByCategoryIdAndTotalCaloriesBetweenAndUnitPriceBetween(String id, Long minCalories,
                                                                                Long maxCalories, BigDecimal minPrice,
                                                                                BigDecimal maxPrice,
                                                                                Pageable pageable);

    Iterable<Product> findAllByCategoryId(String categoryId);

    boolean existsByName(String name);

    boolean existsById(String id);

//    @Modifying
//    @Query("update Product p set p.name = ?1, p.totalCalories = ?2 where u.id = ?3")
//    void setUserInfoById(String firstname, String lastname, Integer userId);

    @Transactional
    void deleteProductById(String id);
}
