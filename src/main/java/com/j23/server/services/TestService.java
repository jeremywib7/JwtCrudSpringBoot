package com.j23.server.services;

import com.j23.server.models.product.ProductCategory;
import com.j23.server.repos.product.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
public class TestService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @PostConstruct
    public void createTestProduct() {

        // create 2 sample category

        ProductCategory breakfast = new ProductCategory();
        breakfast.setId("breakfast");
        breakfast.setCategoryName("Breakfast");
        breakfast.setCreatedOn(LocalDateTime.now());
        breakfast.setUpdatedOn(LocalDateTime.now());
        productCategoryRepository.save(breakfast);

        ProductCategory dinner = new ProductCategory();
        breakfast.setId("dinner");
        breakfast.setCategoryName("Dinner");
        breakfast.setCreatedOn(LocalDateTime.now());
        breakfast.setUpdatedOn(LocalDateTime.now());
        productCategoryRepository.save(dinner);

        // create



    }
}
