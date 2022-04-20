package com.j23.server.services.customer;

import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.repos.customer.CustomerProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class CustomerProfileService {

    @Autowired
    private CustomerProfileRepo customerProfileRepo;

    public CustomerProfile registerCustomer(CustomerProfile customerProfile) {

        if (customerProfileRepo.existsByUsername(customerProfile.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists !");
        }

        customerProfile.setCreatedOn(LocalDateTime.now());
        return customerProfileRepo.save(customerProfile);

    }
}
