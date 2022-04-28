package com.j23.server.controllers.customer.customerOrder;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.customer.OrderedProduct;
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
  private ResponseEntity<Object> addOrder(
    @RequestParam String customerId,
    @RequestBody List<OrderedProduct> orderedProductList) {
    CustomerOrder customerOrder = customerOrderService.addOrder(customerId, orderedProductList);

    return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, customerOrder);
  }

}
