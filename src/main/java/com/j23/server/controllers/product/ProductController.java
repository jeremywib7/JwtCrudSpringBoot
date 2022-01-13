package com.j23.server.controllers.product;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.auth.User;
import com.j23.server.models.product.Product;
import com.j23.server.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllProducts() {
        List<Product> products = (List<Product>) productService.findAllProduct();
        return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, products);
    }

}
