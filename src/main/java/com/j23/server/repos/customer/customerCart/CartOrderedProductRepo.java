package com.j23.server.repos.customer.customerCart;

import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerCart.CartOrderedProduct;
import com.j23.server.models.customer.customerOrder.HistoryProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartOrderedProductRepo extends JpaRepository<CartOrderedProduct, String> {

  @Transactional
  void deleteByCustomerProfileIdAndProductId(String customerProfile_id, String product_id);

  boolean existsByCustomerProfile(CustomerProfile customerProfile);

  List<CartOrderedProduct> findAllByProductId(String id);
}
