package com.j23.server.repos.customer;

import com.j23.server.models.customer.CustomerCart;
import com.j23.server.models.customer.OrderedProduct;
import com.j23.server.models.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedProductRepo extends JpaRepository<OrderedProduct, String> {

  boolean existsByProductAndId(Product product, String id);

}
