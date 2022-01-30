package com.j23.server.repos.product;

import com.j23.server.models.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    boolean existsByCategoryName(String categoryName);

}
