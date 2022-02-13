package com.j23.server.controllers.product;

import com.fasterxml.jackson.annotation.JsonView;
import com.j23.server.configuration.ResponseHandler;
import com.j23.server.exception.ProductNotFoundException;
import com.j23.server.models.product.Product;
import com.j23.server.models.product.Views;
import com.j23.server.repos.product.ProductRepository;
import com.j23.server.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllProductsWithFilter(
            @RequestParam(defaultValue = "0") Long minCalories,
            @RequestParam(defaultValue = "10000") Long maxCalories,
            @RequestParam(defaultValue = "0.00") BigDecimal minPrice,
            @RequestParam(defaultValue = "1000000.00") BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (size < 5 || size > 50) {
            size = 10;
        }

        Page<Product> products = productService.findAllProduct(PageRequest.of(page, size), minCalories,
                maxCalories, minPrice, maxPrice);
        return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, products);
    }

    @PostMapping({"/add"})
    public ResponseEntity<Object> addProduct(
            @RequestBody Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product name already exists");
        }

        Product result = productService.addProduct(product);

        return ResponseHandler.generateResponse("Successfully added product!", HttpStatus.OK, result);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateProduct(@RequestBody Product product) {
        Product updateProduct = productService.updateProduct(product);
        return ResponseHandler.generateResponse("Successfully update product!", HttpStatus.OK, updateProduct);
    }

    @GetMapping({"/findByNameAutoComplete"})
    @JsonView(Views.MyResponseViews.class)
    public ResponseEntity<Object> getProductsNameAutoComplete(@RequestParam String name) {
        Iterable<Product> product = productService.findAllProductByNameAutoComplete(name);
        return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, product);
    }

    @GetMapping({"/findById"})
    public ResponseEntity<Object> getProductById(@RequestParam String id) {
        Optional<Product> product = productService.findProductById(id);
        return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, product);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteProductByName(@PathVariable("id") String id) {

        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        productService.deleteProductById(id);
        return ResponseHandler.generateResponse("Successfully delete product!", HttpStatus.OK,
                null);

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
    public ResponseEntity<Object> getProductsByFilter(@RequestParam String categoryId,
                                                      @RequestParam(defaultValue = "0") Long minCalories,
                                                      @RequestParam(defaultValue = "10000") Long maxCalories,
                                                      @RequestParam(defaultValue = "0.00") BigDecimal minPrice,
                                                      @RequestParam(defaultValue = "1000000.00") BigDecimal maxPrice,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {

        if (size < 5 || size > 50) {
            size = 10;
        }

        Page<Product> products = productService.findAllProductByFilter(
                categoryId, PageRequest.of(page, size), minCalories, maxCalories, minPrice, maxPrice);
        return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, products);
    }

}
