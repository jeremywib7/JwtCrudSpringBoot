package com.j23.server.repos.customer.customerOrder;

import com.j23.server.models.customer.CustomerCart;
import com.j23.server.models.customer.customerOrder.OrderProductList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductListRepo extends JpaRepository<OrderProductList, String> {
}
