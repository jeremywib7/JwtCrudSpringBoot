package com.j23.server.repos.auth;

import com.j23.server.models.auth.Customer;
import com.j23.server.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, String> {

    Customer findByUsername(String username);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByEmailAndPassword(String email, String password);
}
