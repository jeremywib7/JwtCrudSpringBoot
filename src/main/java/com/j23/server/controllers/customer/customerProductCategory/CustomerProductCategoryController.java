package com.j23.server.controllers.customer.customerProductCategory;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.product.Product;
import com.j23.server.services.product.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer/product-category")
public class CustomerProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllProductCategoryInDropdown() {
        return ResponseHandler.generateResponse("Successfully fetch product category for dropdowns!", HttpStatus.OK,
                productCategoryService.getAllProductCategoryInDropdown());
    }
}
