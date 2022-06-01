package com.j23.server.controllers.product;

import com.fasterxml.jackson.annotation.JsonView;
import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.product.Product;
import com.j23.server.models.product.UnassignedProduct;
import com.j23.server.Views;
import com.j23.server.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

  @PostConstruct
    public void onInit() {

      productService.createTestProduct();
    }

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

    @GetMapping("/all/table")
    public ResponseEntity<Object> getAllProductsForTable(
            @RequestParam(defaultValue = "") String searchKeyword,
            @RequestParam(defaultValue = "name") String sortedFieldName,
            @RequestParam(defaultValue = "1") int order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Product> products = productService.findAllProductForTable(searchKeyword, page, size, sortedFieldName, order);
        return ResponseHandler.generateResponse("Successfully fetch product in a table!", HttpStatus.OK, products);
    }

    // set required jwt to false
    // because this for customer
    @GetMapping("/customer/all/table")
    public ResponseEntity<Object> getAllProductsForCustomer(
            @RequestParam(defaultValue = "") String searchKeyword,
            @RequestParam(defaultValue = "name") String sortedFieldName,
            @RequestParam(defaultValue = "1") int order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Product> products = productService.findAllProductForTable(searchKeyword, page, size, sortedFieldName, order);
        return ResponseHandler.generateResponse("Successfully fetch product in a table!", HttpStatus.OK, products);
    }


    @PostMapping({"/add"})
    public ResponseEntity<Object> addProduct(
            @RequestBody Product product) {
        Product result = productService.addProduct(product);

        return ResponseHandler.generateResponse("Successfully added product!", HttpStatus.OK, result);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateProduct(@RequestBody Product product) {
        Product updateProduct = productService.updateProduct(product);
        return ResponseHandler.generateResponse("Successfully update product!", HttpStatus.OK, updateProduct);
    }

    @GetMapping({"/remove"})
    public ResponseEntity<Object> removeProductInCategory(@RequestParam("pId") String pId) {

        Product result = productService.removeProductInCategory(pId);

        return ResponseHandler.generateResponse("Successfully removed product in category!", HttpStatus.OK, result);
    }

    @PutMapping({"/unassigned/update"})
    public ResponseEntity<Object> updateUnassignedProduct(@RequestBody List<UnassignedProduct> unassignedProduct) {
        List<Product> unassignedProducts = productService.updateUnassignedProduct(unassignedProduct);
        return ResponseHandler.generateResponse("Successfully update unassigned product!", HttpStatus.OK,
                unassignedProducts);
    }

    @GetMapping({"/findNameOnly/byCategory"})
    @JsonView(Views.ProductNameViews.class)
    public ResponseEntity<Object> getProductNameOnlyByCategory(@RequestParam String id) {
        Iterable<Product> product = productService.findAllProductNameOnlyByCategory(id);
        return ResponseHandler.generateResponse("Successfully fetch product by category!", HttpStatus.OK, product);
    }

//    @GetMapping({"/count/productByCategory"})
//    @JsonView(Views.ProductNameViews.class)
//    public ResponseEntity<Object> getTotalProductByCategory(@RequestParam String id) {
//        Integer product = productService.getTotalProductOnCategory(id);
//        Map<String, Object> map = new LinkedHashMap<>();
//        map.put("totalProduct", String.valueOf(product));
//
//        return ResponseHandler.generateResponse("Successfully fetch total product!", HttpStatus.OK, map);
//    }

    @GetMapping({"/uuid"})
    public ResponseEntity<Object> getUUID() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("uuid", String.valueOf(UUID.randomUUID()));

        return ResponseHandler.generateResponse("Successfully get UUID!", HttpStatus.OK, map);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteProductByName(@PathVariable("id") String id) {

        productService.deleteProductById(id);
        return ResponseHandler.generateResponse("Successfully delete product!", HttpStatus.OK,
                null);

    }

    @DeleteMapping("/delete/selected")
    public ResponseEntity<Object> deleteSelectedProducts(@RequestParam List<String> id) {

        productService.deleteSelectedProducts(id);
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
