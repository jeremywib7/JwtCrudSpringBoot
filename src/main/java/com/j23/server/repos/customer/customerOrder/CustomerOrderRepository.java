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

    Optional<CustomerOrder> findByCustomerProfileAndStatusEquals(CustomerProfile customerProfile, String status);

    List<CustomerOrder> findAllByCustomerProfile(CustomerProfile customerProfile);

    Optional<CustomerOrder> findFirstByStatusNotAndDateCreatedBetweenOrderByDateCreatedDesc(
            String status, LocalDateTime dateStart, LocalDateTime dateEnd);
}
