package com.j23.server.repos.customer.customerOrder;

import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
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

  int countAllByOrderFinishedBetweenOrderByOrderFinishedDesc(LocalDateTime dateStart, LocalDateTime dateFinished);

  @Query(value = "SELECT SUM(co.total_price) FROM customer_order co WHERE co.order_finished is not null " +
    "AND co.order_finished >= :startDate AND co.order_finished <= :endDate order by co.order_finished desc", nativeQuery = true)
  BigDecimal totalReveneu(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);



}
