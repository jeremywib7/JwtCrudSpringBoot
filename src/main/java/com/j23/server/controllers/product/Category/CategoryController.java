package com.j23.server.controllers.product.Category;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.product.ProductCategory;
import com.j23.server.services.product.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllProductCategory() {
        List<ProductCategory> categories = (List<ProductCategory>) productCategoryService.findAllProductCategory();
        return ResponseHandler.generateResponse("Successfully fetch category!", HttpStatus.OK, categories);
    }

}
