package com.j23.server.controllers.customerOrder;

import com.fasterxml.jackson.annotation.JsonView;
import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.customerOrder.CustomerOrder;
import com.j23.server.models.product.Views;
import com.j23.server.services.customerOrder.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customerOrder")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    @GetMapping("/all")
    public ResponseEntity<Object> findAllCustomerOrder() {
        List<CustomerOrder> customerOrderList = customerOrderService.findAllCustomerOrder();
        return ResponseHandler.generateResponse("Successfully fetch all customer order!", HttpStatus.OK,
                customerOrderList);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addCustomerOrder(@RequestBody CustomerOrder customerOrder) {
        CustomerOrder response = customerOrderService.addCustomerOrder(customerOrder);
        return ResponseHandler.generateResponse("Successfully add order!", HttpStatus.OK,
                response);
    }

}
