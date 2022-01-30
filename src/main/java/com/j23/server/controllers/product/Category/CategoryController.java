package com.j23.server.controllers.product.Category;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.auth.Role;
import com.j23.server.models.product.Product;
import com.j23.server.models.product.ProductCategory;
import com.j23.server.repos.product.ProductCategoryRepository;
import com.j23.server.repos.product.ProductRepository;
import com.j23.server.services.product.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllProductCategory() {
        List<ProductCategory> categories = (List<ProductCategory>) productCategoryService.findAllProductCategory();
        return ResponseHandler.generateResponse("Successfully fetch category!", HttpStatus.OK, categories);
    }

    @PostMapping({"/add"})
    public ResponseEntity<Object> addNewCategory(@RequestBody ProductCategory productCategory) {

        ProductCategory result = productCategoryService.addProductCategory(productCategory);
        return ResponseHandler.generateResponse("Successfully added category!", HttpStatus.OK, result);
    }

}
