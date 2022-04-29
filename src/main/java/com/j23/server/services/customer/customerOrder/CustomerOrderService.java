package com.j23.server.services.customer.customerOrder;

import com.j23.server.models.customer.CustomerCart;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.OrderedProduct;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.models.customer.customerOrder.OrderProductList;
import com.j23.server.repos.customer.customerOrder.CustomerOrderRepository;
import com.j23.server.repos.customer.customerOrder.OrderProductListRepo;
import com.j23.server.services.customer.CustomerCartService;
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
    private OrderProductListRepo orderProductListRepo;


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

        List<OrderProductList> orderProductLists = new ArrayList<>();

        // set in order product list
        customerCart.getOrderedProduct().forEach(orderedProduct -> {
            OrderProductList orderProductList = new OrderProductList();
            orderProductList.setId(String.valueOf(UUID.randomUUID()));
            orderProductList.setProduct(orderedProduct.getProduct());
            orderProductList.setName(orderedProduct.getProduct().getName());
            orderProductList.setQuantity(orderedProduct.getQuantity());
            orderProductList.setDiscount(orderedProduct.getProduct().isDiscount());
            orderProductList.setUnitPrice(orderedProduct.getProduct().getUnitPrice());
            orderProductList.setDiscountedPrice(orderedProduct.getProduct().getDiscountedPrice());

            orderProductLists.add(orderProductList);
            orderProductListRepo.save(orderProductList);
        });

        customerOrder.setOrderProductLists(orderProductLists);

        return customerOrderRepository.save(customerOrder);

    }

}
