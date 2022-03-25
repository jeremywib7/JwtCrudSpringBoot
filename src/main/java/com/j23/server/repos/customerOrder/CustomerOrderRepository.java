package com.j23.server.repos.customerOrder;

import com.j23.server.models.customerOrder.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, String> {
}
