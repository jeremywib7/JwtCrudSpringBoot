package com.j23.server.controllers.restaurant.product.Category;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.product.ProductCategory;
import com.j23.server.models.product.UnassignedProduct;
import com.j23.server.services.restaurant.product.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostConstruct
    public void initRolesAndUsers() {
        productCategoryService.addUnassignedCategory();
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllProductCategory() {
        List<ProductCategory> categories = productCategoryService.findAllProductCategory();
        return ResponseHandler.generateResponse("Successfully fetch category!", HttpStatus.OK, categories);
    }

    @PostMapping({"/add"})
    public ResponseEntity<Object> addNewCategory(@RequestBody ProductCategory productCategory) {

        ProductCategory result = productCategoryService.addProductCategory(productCategory);
        return ResponseHandler.generateResponse("Successfully added category!", HttpStatus.OK, result);
    }

    @PutMapping({"/update/name"})
    public ResponseEntity<Object> updateCategory(@RequestBody ProductCategory productCategory) {

        ProductCategory result = productCategoryService.updateProductCategory(productCategory);
        return ResponseHandler.generateResponse("Successfully updated category name!", HttpStatus.OK, result);
    }

    @PutMapping({"/update/productList"})
    public ResponseEntity<Object> updateUnassignedProductList(@RequestBody List<UnassignedProduct> unassignedProduct) {
        List<ProductCategory> productList = productCategoryService.updateUnassignedProductCategory(unassignedProduct);
        return ResponseHandler.generateResponse("Successfully updated unassigned product!", HttpStatus.OK, productList);
    }

    @GetMapping({"/delete"})
    public ResponseEntity<Object> deleteCategory(@RequestParam(required = false) List<String> id, @RequestParam String productCategoryId) {
        productCategoryService.deleteProductCategory(id, productCategoryId);
        return ResponseHandler.generateResponse("Successfully delete category!", HttpStatus.OK, null);
    }

}
