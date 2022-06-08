package com.j23.server.repos.customer;

import com.j23.server.models.customer.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CustomerProfileRepo extends JpaRepository<CustomerProfile, String> {
    boolean existsByUsername(String username);

    Optional<CustomerProfile> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update CustomerProfile cu SET cu.messagingToken=:messagingToken WHERE cu.id =:customerId")
    void updateCustomerMessagingToken(@Param(value = "messagingToken") String messagingToken,
                                      @Param(value = "customerId") String customerId);

//    int countAllByCreatedOnBetween(LocalDateTime startDate, LocalDateTime endDate);

}
