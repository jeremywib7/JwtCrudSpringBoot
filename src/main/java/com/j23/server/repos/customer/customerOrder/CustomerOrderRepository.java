package com.j23.server.repos.customer.customerOrder;

import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, String> {

  Optional<CustomerOrder> findTopByCustomerProfileAndOrderIsActiveTrueOrderByDateTimeCreatedDesc(CustomerProfile customerProfile);

  Optional<CustomerOrder> findTopByCustomerProfileAndOrderProcessedIsNullOrderByDateTimeCreatedDesc(CustomerProfile customerProfile);

  Optional<CustomerOrder> findByCustomerProfileAndOrderIsActiveTrue(CustomerProfile customerProfile);

  List<CustomerOrder> findAllByCustomerProfileOrderByDateTimeCreatedDesc(CustomerProfile customerProfile);

  Optional<CustomerOrder> findFirstByOrderProcessedIsNotNullAndDateTimeCreatedBetweenOrderByDateTimeCreatedDesc(
    LocalDateTime dateStart, LocalDateTime dateEnd);

  List<CustomerOrder> findAllByOrderFinishedIsNotNullAndOrderFinishedBetweenOrderByOrderFinishedDesc(
    LocalDateTime dateStart, LocalDateTime dateEnd);

  List<CustomerOrder> findAllByOrderFinishedIsNotNull();
}
