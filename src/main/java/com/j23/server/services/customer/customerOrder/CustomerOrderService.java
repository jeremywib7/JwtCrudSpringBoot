package com.j23.server.services.customer.customerOrder;

import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.OrderedProduct;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.models.customer.customerOrder.OrderProductList;
import com.j23.server.repos.customer.customerOrder.CustomerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerOrderService {

  @Autowired
  private CustomerOrderRepository customerOrderRepository;

  public CustomerOrder addOrder(String customerId, List<OrderedProduct> orderedProductList) {

    CustomerOrder customerOrder = new CustomerOrder();
    customerOrder.setId(String.valueOf(UUID.randomUUID()));

    CustomerProfile customerProfile = new CustomerProfile();
    customerProfile.setId(customerId);
    customerOrder.setCustomerProfile(customerProfile);

    List<OrderProductList> completeOrderProductList = new ArrayList<>();

    orderedProductList.forEach(value -> {
      OrderProductList orderProductList = new OrderProductList();
    });

    customerOrder.setOrderProductLists(completeOrderProductList);

    return customerOrderRepository.save(customerOrder);

  }

}
