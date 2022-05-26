package com.j23.server.controllers.customer.customerOrder;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.services.customer.customerOrder.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class CustomerOrderController {

  @Autowired
  private CustomerOrderService customerOrderService;

  @PostMapping("/add")
  private ResponseEntity<Object> addOrder(@RequestParam String customerId) {
    CustomerOrder customerOrder = customerOrderService.addOrder(customerId);

    return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, customerOrder);
  }

  @GetMapping("/view")
  private ResponseEntity<Object> viewCustomerOrdersByCustomerId(@RequestParam String customerId) {
    List<CustomerOrder> customerOrders = customerOrderService.viewCustomerOrdersByCustomerId(customerId);

    return ResponseHandler.generateResponse("Successfully view customer orders!", HttpStatus.OK, customerOrders);
  }

  @GetMapping("/view/active")
  private ResponseEntity<Object> viewActiveCustomerOrder(@RequestParam String customerId) {
    CustomerOrder customerOrders = customerOrderService.viewCurrentCustomerOrder(customerId);

    return ResponseHandler.generateResponse("Successfully view customer order!", HttpStatus.OK, customerOrders);
  }

  @GetMapping("/view/byUsername/{username}")
  private ResponseEntity<Object> viewCustomerOrdersByCustomerUsername(@PathVariable("username") String username) {
    CustomerOrder customerOrder = customerOrderService.viewCustomerOrderByCustomerUsername(username);

    return ResponseHandler.generateResponse("Successfully view customer orders!", HttpStatus.OK, customerOrder);
  }

  @PostMapping("/pay")
  private ResponseEntity<Object> confirmPayOrder(
    @RequestBody CustomerOrder customerOrder
    ) {
    CustomerOrder response = customerOrderService.confirmPayOrder(customerOrder);
    return ResponseHandler.generateResponse("Successfully confirm order as payed!", HttpStatus.OK, response);
  }

  @PutMapping("/completed")
  private ResponseEntity<Object> confirmPayOrder(
    @RequestParam String customerId
  ) {
    customerOrderService.finishOrder(customerId);
    return ResponseHandler.generateResponse("Customer order successfully completed!", HttpStatus.OK, null);
  }

}

