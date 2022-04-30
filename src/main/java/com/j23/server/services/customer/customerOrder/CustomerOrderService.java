package com.j23.server.services.customer.customerOrder;

import com.j23.server.models.customer.customerCart.CustomerCart;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.models.customer.customerOrder.HistoryProductOrder;
import com.j23.server.repos.customer.customerCart.CustomerCartRepository;
import com.j23.server.repos.customer.customerOrder.CustomerOrderRepository;
import com.j23.server.repos.customer.customerOrder.HistoryProductOrderRepo;
import com.j23.server.services.customer.customerCart.CustomerCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerOrderService {

  @Autowired
  private CustomerOrderRepository customerOrderRepository;

  @Autowired
  private CustomerCartRepository customerCartRepository;

  @Autowired
  private CustomerCartService customerCartService;

  @Autowired
  private HistoryProductOrderRepo historyProductOrderRepo;

  public CustomerOrder addOrder(String customerId) {

    // get customer cart info
    CustomerCart customerCart = customerCartService.getCustomerCart(customerId);
    customerCart.setPlacedInOrder(true);
    customerCartRepository.save(customerCart);

    // create customer order
    CustomerOrder customerOrder = new CustomerOrder();

    // set customer profile
    CustomerProfile customerProfile = new CustomerProfile();
    List<HistoryProductOrder> historyProductOrders = new ArrayList<>();
    BigDecimal totalPrice = BigDecimal.valueOf(0);

    // store list of product price
    List<BigDecimal> prices = new ArrayList<>(List.of());

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

      // add total price
      prices.add(orderedProduct.getProduct().getDiscountedPrice().multiply(new BigDecimal(orderedProduct.getQuantity())));
    });

    customerOrder.setId(String.valueOf(UUID.randomUUID()));
    customerProfile.setId(customerId);
    customerOrder.setCustomerProfile(customerProfile);
    customerOrder.setTotalPrice(totalPrice);
    customerOrder.setHistoryProductOrders(historyProductOrders);

    // sum list of discounted prices to set as totalPrice in database
    BigDecimal totalPriceFinal = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    customerOrder.setTotalPrice(totalPriceFinal);

    return customerOrderRepository.save(customerOrder);

  }

}
