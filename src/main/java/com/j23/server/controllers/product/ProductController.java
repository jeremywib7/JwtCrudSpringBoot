package com.j23.server.controllers.product;

import com.fasterxml.jackson.annotation.JsonView;
import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.product.Product;
import com.j23.server.models.product.Views;
import com.j23.server.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllProducts(
            @RequestParam(defaultValue = "0") Long minCalories,
            @RequestParam(defaultValue = "10000") Long maxCalories,
            @RequestParam(defaultValue = "0.00") BigDecimal minPrice,
            @RequestParam(defaultValue = "1000000.00") BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (size < 10 || size > 50) {
            size = 10;
        }

        Page<Product> products = productService.findAllProduct(PageRequest.of(page, size), minCalories,
                maxCalories, minPrice, maxPrice);
        return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, products);
    }

    @GetMapping({"/findByNameAutoComplete"})
    @JsonView(Views.MyResponseViews.class)
    public ResponseEntity<Object> getProductsNameAutoComplete(@RequestParam String name) {
        Iterable<Product> product = productService.findAllProductByNameAutoComplete(name);
        return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, product);
    }

    @GetMapping({"/findById"})
    public ResponseEntity<Object> getProductById(@RequestParam Long id) {
        Optional<Product> product = productService.findProductById(id);
        return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, product);
    }

    @GetMapping({"/findByName"})
    public ResponseEntity<Object> getProductsByName(@RequestParam String name,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        if (size < 10 || size > 50) {
            size = 10;
        }

        Page<Product> products = productService.findAllProductByName(
                name, PageRequest.of(page, size));
        return ResponseHandler.generateResponse("Successfully fetch product autocomplete!",
                HttpStatus.OK, products);
    }

    @GetMapping({"/findByCategory"})
    public ResponseEntity<Object> getProductsByFilter(@RequestParam Long categoryId,
                                                      @RequestParam(defaultValue = "0") Long minCalories,
                                                      @RequestParam(defaultValue = "10000") Long maxCalories,
                                                      @RequestParam(defaultValue = "0.00") BigDecimal minPrice,
                                                      @RequestParam(defaultValue = "1000000.00") BigDecimal maxPrice,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {

        if (size < 10 || size > 50) {
            size = 10;
        }

        Page<Product> products = productService.findAllProductByFilter(
                categoryId, PageRequest.of(page, size), minCalories, maxCalories, minPrice, maxPrice);
        return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, products);
    }

}
