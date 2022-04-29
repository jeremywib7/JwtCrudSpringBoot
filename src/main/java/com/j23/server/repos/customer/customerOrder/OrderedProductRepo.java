package com.j23.server.repos.customer.customerOrder;

import com.j23.server.models.customer.customerCart.CartOrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedProductRepo extends JpaRepository<CartOrderedProduct, String> {

  List<CartOrderedProduct> findAllByProductId(String id);
}
