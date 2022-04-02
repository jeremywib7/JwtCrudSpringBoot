package com.j23.server.repos.product;

import com.j23.server.models.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {

    ProductCategory findProductCategoryById(String id);

    boolean existsByCategoryName(String categoryName);

    //    for product category update modal
    boolean existsByCategoryNameAndIdIsNotLike(String name, String id);

    @Modifying
    @Query("update ProductCategory pc set pc.updatedOn = ?1 where pc.id = ?2")
    void updateProductCategoryUpdatedOn(LocalDateTime updatedOn, String productCategoryId);

    @Transactional
    void deleteProductCategoryById(String id);

}
