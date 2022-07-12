package com.j23.server.controllers.customer.customerProductView;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.product.Product;
import com.j23.server.services.restaurant.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/productview")
public class ProductViewController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<Object> getProductById(@RequestParam String name) {

        Product product = productService.findProductByName(name).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return ResponseHandler.generateResponse("Successfully fetch product!",
                HttpStatus.OK, product);
    }
}
