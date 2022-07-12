package com.j23.server.controllers.customer.customerProduct;

import com.fasterxml.jackson.annotation.JsonView;
import com.j23.server.Views;
import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.product.Product;
import com.j23.server.services.restaurant.dashboard.TotalSalesProductService;
import com.j23.server.services.restaurant.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private TotalSalesProductService totalSalesProductService;

    @GetMapping("/product/all")
    public ResponseEntity<Object> getAllProductsForCustomer(
            @RequestParam(required = false) String categoryName,
            @RequestParam(defaultValue = "") String searchKeyword,
            @RequestParam(defaultValue = "name") String sortedFieldName,
            @RequestParam(defaultValue = "1") int order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Product> products = productService.findAllProductForTable(searchKeyword, page, size, sortedFieldName, order
        , categoryName);
        return ResponseHandler.generateResponse("Successfully fetch product in a table!", HttpStatus.OK, products);
    }

    @GetMapping("/best-seller")
    @JsonView(Views.BestSellerOnlyViews.class)
    public ResponseEntity<Object> viewTop5SalesOnly() {
        return ResponseHandler.generateResponse("Successfully view 5 top sales", HttpStatus.OK, totalSalesProductService.viewTop5SalesOnly());
    }
}
