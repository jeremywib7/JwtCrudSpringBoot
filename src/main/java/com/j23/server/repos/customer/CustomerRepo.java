package com.j23.server.repos.customer;

import com.j23.server.models.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, String> {

    Customer findByUsername(String username);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByEmailAndPassword(String email, String password);

    boolean existsByUsername(String username);
}
