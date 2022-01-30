package com.j23.server.services.product;

import com.j23.server.models.product.ProductCategory;
import com.j23.server.repos.product.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public ProductCategory addProductCategory(ProductCategory productCategory) {

        if (productCategoryRepository.existsByCategoryName(productCategory.getCategoryName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product category already exists");
        }

        productCategory.setId(String.valueOf(UUID.randomUUID()));

        return productCategoryRepository.save(productCategory);
    }

    public Iterable<ProductCategory> findAllProductCategory() {
        return productCategoryRepository.findAll();
    }
}
