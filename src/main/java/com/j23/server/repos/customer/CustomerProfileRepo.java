package com.j23.server.repos.customer;

import com.j23.server.models.customer.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerProfileRepo extends JpaRepository<CustomerProfile, String> {

    boolean existsByUsername(String username);
}
