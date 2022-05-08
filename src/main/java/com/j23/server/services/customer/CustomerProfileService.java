package com.j23.server.services.customer;

import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.repos.customer.CustomerProfileRepo;
import com.j23.server.services.customer.customerCart.CustomerCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CustomerProfileService {

    @Autowired
    private CustomerProfileRepo customerProfileRepo;

    @Autowired
    private CustomerCartService customerCartService;

    public CustomerProfile registerCustomer(CustomerProfile customerProfile) {

        if (customerProfileRepo.existsByUsername(customerProfile.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists !");
        }

        CustomerProfile customerProfile1 = customerProfileRepo.save(customerProfile);

        // create cart
        customerCartService.createCart(customerProfile);

        return customerProfile1;

    }

    public CustomerProfile getCustomerById(String customerId) {
        return customerProfileRepo.findById(customerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found !"));
    }

    public CustomerProfile saveCustomerProfile(CustomerProfile customerProfile) {
        return customerProfileRepo.save(customerProfile);
    }
}
