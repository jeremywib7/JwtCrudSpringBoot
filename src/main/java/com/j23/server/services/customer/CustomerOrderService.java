package com.j23.server.services.customer;

import com.j23.server.models.customerOrder.CustomerOrder;
import com.j23.server.repos.customer.CustomerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerOrderService {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    public List<CustomerOrder> findAllCustomerOrder() {
        return customerOrderRepository.findAll();
    }

    public CustomerOrder addCustomerOrder(CustomerOrder customerOrder) {
        customerOrder.setId(String.valueOf(UUID.randomUUID()));

        LocalDateTime localDateTime = LocalDateTime.now();
        customerOrder.setDateTime(LocalDateTime.from(localDateTime));

        return customerOrderRepository.save(customerOrder);
    }

}
