package com.j23.server.controllers.customer;

import com.google.firebase.auth.FirebaseAuthException;
import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerCart.CustomerCart;
import com.j23.server.services.customer.CustomerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerProfileController {

    @Autowired
    private CustomerProfileService customerProfileService;

    @GetMapping({"/profile/{customerId}"})
    private ResponseEntity<Object> getCustomerById(@PathVariable("customerId") String customerId) {
        CustomerProfile customerProfile = customerProfileService.getCustomerById(customerId);

        return ResponseHandler.generateResponse("Success fetch customer profile!", HttpStatus.OK,
                customerProfile);
    }

  @GetMapping({"/profile/username/{username}"})
  private ResponseEntity<Object> getCustomerByUsername(@PathVariable("username") String username) {
    CustomerProfile customerProfile = customerProfileService.getCustomerByUsername(username);

    return ResponseHandler.generateResponse("Success fetch customer profile!", HttpStatus.OK,
      customerProfile);
  }

    @PostMapping({"/register"})
    private ResponseEntity<Object> registerCustomer(@RequestBody CustomerProfile customerProfile) {
        CustomerCart result = customerProfileService.registerCustomer(customerProfile);

        return ResponseHandler.generateResponse("Register success!", HttpStatus.OK, result);
    }

    @PostMapping({"/update/profile"})
    private ResponseEntity<Object> saveCustomerProfile(@RequestBody CustomerProfile customerProfile) {
        CustomerProfile result = customerProfileService.saveCustomerProfile(customerProfile);

        return ResponseHandler.generateResponse("Update profile success!", HttpStatus.OK, result);
    }

}
