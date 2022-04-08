package com.j23.server.repos.product;

import com.j23.server.models.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    Product findProductById(String id);

    Page<Product> findAllByTotalCaloriesBetweenAndUnitPriceBetween(Long minCalories,
                                                                   Long maxCalories, BigDecimal minPrice,
                                                                   BigDecimal maxPrice,
                                                                   Pageable pageable);

    Page<Product> findAllByNameContaining(String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE CONCAT(p.name, ' ', p.active, ' ', " +
            "p.description, ' ',p.totalCalories,' ', p.unitPrice) LIKE %?1%")
    Page<Product> findAllBySearchTable(String searchKeyword, Pageable pageable);

    Page<Product> findAllByCategoryIdAndTotalCaloriesBetweenAndUnitPriceBetween(
            String id, Long minCalories, Long maxCalories, BigDecimal minPrice, BigDecimal maxPrice,
            Pageable pageable);

    List<Product> findAllByCategoryId(String categoryId);

    boolean existsByName(String name);

    boolean existsById(String id);

    Integer countAllByCategoryId(String id);

    @Transactional
    void deleteProductById(String id);

}
