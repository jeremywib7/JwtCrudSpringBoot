package com.j23.server.repos.customer.customerOrder;

import com.j23.server.models.customer.customerOrder.HistoryProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryProductOrderRepo extends JpaRepository<HistoryProductOrder, String> {

  List<HistoryProductOrder> findAllByProductId(String id);
}
