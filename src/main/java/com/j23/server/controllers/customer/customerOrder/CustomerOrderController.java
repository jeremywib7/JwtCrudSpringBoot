package com.j23.server.controllers.customer.customerOrder;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.services.SeederService;
import com.j23.server.services.customer.customerOrder.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/order")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private SeederService seederService;

    @PostMapping("/add")
    private ResponseEntity<Object> addOrder(@RequestParam String customerId) {
        CustomerOrder customerOrder = customerOrderService.addOrder(customerId);

        return ResponseHandler.generateResponse("Successfully fetch product!", HttpStatus.OK, customerOrder);
    }

    @PutMapping("/cancel")
    private ResponseEntity<Object> cancelOrder(@RequestParam String customerId) {
        customerOrderService.cancelOrder(customerId);
        return ResponseHandler.generateResponse("order successfully cancelled!", HttpStatus.OK, null);
    }

    @GetMapping("/view")
    private ResponseEntity<Object> viewCustomerOrdersBetweenDateByCustomerId(
            @RequestParam String customerId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateTill,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseHandler.generateResponse("Successfully view customer orders!", HttpStatus.OK,
                customerOrderService.viewCustomerOrdersBetweenDateByCustomerId(customerId, page, size, dateFrom, dateTill));
    }

    @GetMapping("/view/active")
    private ResponseEntity<Object> viewActiveCustomerOrder(@RequestParam String customerId) {
        CustomerOrder customerOrders = customerOrderService.viewCurrentCustomerOrder(customerId);

        return ResponseHandler.generateResponse("Successfully view active customer order!", HttpStatus.OK, customerOrders);
    }

    @GetMapping("/recent")
    private ResponseEntity<Object> viewRecentCustomerOrder() {
        return ResponseHandler.generateResponse("Successfully view recent customer order!", HttpStatus.OK,
                customerOrderService.viewRecentSales());
    }

    @GetMapping("/view/byUsername/{username}")
    private ResponseEntity<Object> viewCustomerOrdersByCustomerUsername(@PathVariable("username") String username) {
        CustomerOrder customerOrder = customerOrderService.viewCustomerOrderByCustomerUsername(username);

        return ResponseHandler.generateResponse("Successfully view customer orders by username!", HttpStatus.OK, customerOrder);
    }

    @PostMapping("/pay")
    private ResponseEntity<Object> confirmPayOrder(
            @RequestBody CustomerOrder customerOrder
    ) {
        CustomerOrder response = customerOrderService.confirmPayOrder(customerOrder);
        return ResponseHandler.generateResponse("Successfully confirm order as paid!", HttpStatus.OK, response);
    }

    @PutMapping("/completed")
    private ResponseEntity<Object> confirmPayOrder(
            @RequestParam String customerId
    ) {
        customerOrderService.finishOrder(customerId);
        return ResponseHandler.generateResponse("Customer order successfully confirmed!", HttpStatus.OK, null);
    }

  @DeleteMapping("/delete/waiting-list")
  private ResponseEntity<Object> deleteWaitingListFirebase(
    @RequestParam String customerId
  ) {
    customerOrderService.deleteWaitingListFirebase(customerId);
    return ResponseHandler.generateResponse("Success delete waiting list!", HttpStatus.OK, null);
  }

}

