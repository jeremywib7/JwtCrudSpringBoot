package com.j23.server.controllers.customer;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.customer.Customer;
import com.j23.server.services.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping({"/register"})
    private ResponseEntity<Object> registerCustomer(@RequestBody Customer customer) {
        Customer result = customerService.registerCustomer(customer);

        return ResponseHandler.generateResponse("Register success!", HttpStatus.OK, result);
    }
}
