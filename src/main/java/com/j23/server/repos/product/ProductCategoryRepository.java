package com.j23.server.repos.product;

import com.j23.server.models.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {

    boolean existsById(String id);

    boolean existsByCategoryName(String categoryName);

    @Transactional
    void deleteProductCategoryById(String id);

}
