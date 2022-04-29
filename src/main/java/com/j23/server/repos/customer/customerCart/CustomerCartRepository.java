package com.j23.server.repos.customer.customerCart;

import com.j23.server.models.customer.customerCart.CustomerCart;
import com.j23.server.models.customer.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerCartRepository extends JpaRepository<CustomerCart, String> {

  Optional<CustomerCart> findByCustomerProfile(CustomerProfile customerProfile);

//  Optional<OrderedProduct> findByCustomerProfile(CustomerProfile customerProfile);


}
