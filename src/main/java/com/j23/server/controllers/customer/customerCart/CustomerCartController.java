package com.j23.server.controllers.customer.customerCart;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.customer.customerCart.CustomerCart;
import com.j23.server.models.customer.customerCart.CartOrderedProduct;
import com.j23.server.services.customer.customerCart.CustomerCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CustomerCartController {

  @Autowired
  private CustomerCartService customerCartService;


  @GetMapping("/view")
  public ResponseEntity<Object> viewCart(@RequestParam String customerId) {
    CustomerCart customerCart = customerCartService.viewCart(customerId);
    return ResponseHandler.generateResponse("Successfully fetch cart!", HttpStatus.OK,
      customerCart);
  }

  @PostMapping("/add")
  public ResponseEntity<Object> addProductToCart(
    @RequestParam String customerId,
    @RequestParam String productId,
    @RequestParam Integer productQuantity
  ) {
    CartOrderedProduct response = customerCartService.addProductToCart(customerId, productId, productQuantity);
    return ResponseHandler.generateResponse("Successfully add product to cart!", HttpStatus.OK,
      response);
  }

  @PostMapping("/update")
  public ResponseEntity<Object> updateProductInCart(
    @RequestParam String customerId,
    @RequestParam String productId,
    @RequestParam Integer productQuantity
  ) {
    CustomerCart response = customerCartService.updateProductQuantityInCart(customerId, productId, productQuantity);
    return ResponseHandler.generateResponse("Successfully update product in cart!", HttpStatus.OK,
      response);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Object> removeProductFromCart(
    @RequestParam String customerId,
    @RequestParam String productId
  ) {
    CustomerCart response = customerCartService.removeProductFromCart(customerId, productId);
    return ResponseHandler.generateResponse("Successfully delete product from cart!", HttpStatus.OK,
      response);
  }

}
