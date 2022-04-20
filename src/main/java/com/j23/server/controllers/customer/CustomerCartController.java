package com.j23.server.controllers.customer;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.customer.CustomerCart;
import com.j23.server.services.customer.CustomerCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CustomerCartController {

    @Autowired
    private CustomerCartService customerCartService;

    @GetMapping("/all")
    public ResponseEntity<Object> findAllCustomerOrder() {
        List<CustomerCart> customerCartList = customerCartService.findAllCustomerOrder();
        return ResponseHandler.generateResponse("Successfully fetch all customer order!", HttpStatus.OK,
                customerCartList);
    }

    @PostMapping("/add/product")
    public ResponseEntity<Object> addProductToCart(
            @RequestParam String customerId,
            @RequestParam String productId,
            @RequestParam Integer productQuantity
    ) {
        CustomerCart response = customerCartService.addProductToCart(customerId, productId, productQuantity);
        return ResponseHandler.generateResponse("Successfully add product to cart!", HttpStatus.OK,
                response);
    }

    @PostMapping("/update/product/quantity")
    public ResponseEntity<Object> updateProductQtyInCart(
            @RequestParam String customerId,
            @RequestParam String productId,
            @RequestParam Integer productQuantity
    ) {
        CustomerCart response = customerCartService.updateProductQuantityInCart(customerId, productId, productQuantity);
        return ResponseHandler.generateResponse("Successfully update product quantity!", HttpStatus.OK,
                response);
    }

    @PostMapping("/delete/product")
    public ResponseEntity<Object> deleteProductInCart(
            @RequestParam String customerId,
            @RequestParam String productId,
            @RequestParam Integer productQuantity
    ) {
        CustomerCart response = customerCartService.addProductToCart(customerId, productId, productQuantity);
        return ResponseHandler.generateResponse("Successfully update product quantity!", HttpStatus.OK,
                response);
    }

}
