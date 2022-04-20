package com.j23.server.controllers.customer;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.services.customer.CustomerCartService;
import com.j23.server.services.customer.CustomerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerDetailController {

    @Autowired
    private CustomerProfileService customerProfileService;

    @Autowired
    private CustomerCartService customerCartService;

    @PostMapping({"/register"})
    private ResponseEntity<Object> registerCustomer(@RequestBody CustomerProfile customerProfile) {
        CustomerProfile result = customerProfileService.registerCustomer(customerProfile);
        customerCartService.createCart(customerProfile.getId());

        return ResponseHandler.generateResponse("Register success!", HttpStatus.OK, result);
    }
}
