package com.j23.server.services.customer.customerOrder;

import com.j23.server.models.customer.customerCart.CustomerCart;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.models.customer.customerOrder.HistoryProductOrder;
import com.j23.server.repos.customer.customerOrder.CustomerOrderRepository;
import com.j23.server.repos.customer.customerOrder.HistoryProductOrderRepo;
import com.j23.server.services.customer.customerCart.CustomerCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerOrderService {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private CustomerCartService customerCartService;

    @Autowired
    private HistoryProductOrderRepo historyProductOrderRepo;

    public CustomerOrder addOrder(String customerId) {
        // get customer cart info
        CustomerCart customerCart = customerCartService.getCustomerCart(customerId);

        // create customer order
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(String.valueOf(UUID.randomUUID()));

        // set customer profile
        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setId(customerId);
        customerOrder.setCustomerProfile(customerProfile);

        List<HistoryProductOrder> historyProductOrders = new ArrayList<>();

        // set in order product list
        customerCart.getCartOrderedProduct().forEach(orderedProduct -> {
            HistoryProductOrder historyProductOrder = new HistoryProductOrder();
            historyProductOrder.setId(String.valueOf(UUID.randomUUID()));
            historyProductOrder.setProduct(orderedProduct.getProduct());
            historyProductOrder.setName(orderedProduct.getProduct().getName());
            historyProductOrder.setQuantity(orderedProduct.getQuantity());
            historyProductOrder.setDiscount(orderedProduct.getProduct().isDiscount());
            historyProductOrder.setUnitPrice(orderedProduct.getProduct().getUnitPrice());
            historyProductOrder.setDiscountedPrice(orderedProduct.getProduct().getDiscountedPrice());

            historyProductOrders.add(historyProductOrder);
            historyProductOrderRepo.save(historyProductOrder);
        });

        customerOrder.setHistoryProductOrders(historyProductOrders);

        return customerOrderRepository.save(customerOrder);

    }

}
