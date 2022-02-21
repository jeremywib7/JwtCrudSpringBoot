package com.j23.server.repos.product;

import com.j23.server.models.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {

    boolean existsById(String id);

    boolean existsByCategoryName(String categoryName);

    //    for product category update modal
    boolean existsByCategoryNameAndIdIsNotLike(String name, String id);

    @Transactional
    void deleteProductCategoryById(String id);

}
