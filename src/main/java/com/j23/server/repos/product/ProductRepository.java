package com.j23.server.repos.product;

import com.j23.server.models.product.ProductSteps.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(String id);

    Page<Product> findAll(Pageable pageable);

    Product findProductById(String id);

    Page<Product> findAllByProductDetailTotalCaloriesBetweenAndProductPricingUnitPriceBetween(Long minCalories,
                                                                                              Long maxCalories, BigDecimal minPrice,
                                                                                              BigDecimal maxPrice,
                                                                                              Pageable pageable);

    Page<Product> findAllByProductDetailNameContaining(String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE CONCAT(p.productDetail.name, ' ', p.productDetail.active, ' ', " +
            "p.productDetail.description, ' ',p.productDetail.totalCalories,' ', p.productPricing.unitPrice) LIKE %?1%")
    Page<Product> findAllBySearchTable(String searchKeyword, Pageable pageable);

    Page<Product> findAllByProductDetailCategoryIdAndProductDetailTotalCaloriesBetweenAndProductPricingUnitPriceBetween(
            String id, Long minCalories, Long maxCalories, BigDecimal minPrice, BigDecimal maxPrice,
            Pageable pageable);

    Iterable<Product> findAllByProductDetailCategoryId(String categoryId);

    boolean existsByProductDetailName(String name);

    boolean existsById(String id);

    Integer countAllByProductDetailCategoryId(String id);

    @Transactional
    void deleteProductById(String id);
}
