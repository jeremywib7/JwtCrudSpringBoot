package com.j23.server.controllers.product;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.auth.User;
import com.j23.server.models.product.Product;
import com.j23.server.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllProducts(@RequestParam int page,
                                                 @RequestParam(defaultValue = "0") int size) {

        if (size == 0) {
            size = 10;
        }

        Page<Product> products = (Page<Product>) productService.findAllProduct(PageRequest.of(page, size));
        return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, products);
    }

    @GetMapping({"/findByCategory"})
    public ResponseEntity<Object> getProductsByCategory(@RequestParam Long id,
                                                  @RequestParam int page,
                                                  @RequestParam(defaultValue = "0") int size) {
        if (size == 0) {
            size = 10;
        }

        Page<Product> products = (Page<Product>) productService.findAllProductByCategoryId(
                id, PageRequest.of(page, size));
        return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, products);
    }

}
