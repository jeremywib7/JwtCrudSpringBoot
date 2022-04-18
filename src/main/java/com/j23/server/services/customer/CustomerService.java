package com.j23.server.services.customer;

import com.j23.server.models.customer.Customer;
import com.j23.server.repos.customer.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public Customer registerCustomer(Customer customer) {

        if (customerRepo.existsByUsername(customer.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists !");
        }

        customer.setCreatedOn(LocalDateTime.now());
        return customerRepo.save(customer);
    }
}
