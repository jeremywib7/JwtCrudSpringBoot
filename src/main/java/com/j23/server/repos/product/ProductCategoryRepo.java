package com.j23.server.repos.product;

import com.j23.server.models.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepo extends JpaRepository<ProductCategory, Long> {
}
