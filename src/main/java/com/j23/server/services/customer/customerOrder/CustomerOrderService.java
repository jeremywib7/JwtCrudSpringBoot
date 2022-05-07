package com.j23.server.services.customer.customerOrder;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.j23.server.models.customer.customerCart.CustomerCart;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.models.customer.customerOrder.HistoryProductOrder;
import com.j23.server.models.waitingList.WaitingList;
import com.j23.server.repos.customer.CustomerProfileRepo;
import com.j23.server.repos.customer.customerCart.CustomerCartRepository;
import com.j23.server.repos.customer.customerOrder.CustomerOrderRepository;
import com.j23.server.repos.customer.customerOrder.HistoryProductOrderRepo;
import com.j23.server.services.customer.customerCart.CustomerCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class CustomerOrderService {

  @Autowired
  private CustomerOrderRepository customerOrderRepository;

  @Autowired
  private CustomerProfileRepo customerProfileRepository;

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

    customerProfile.setId(customerId);
    customerOrder.setCustomerProfile(customerProfile);
    customerOrder.setTotalPrice(totalPrice);
    customerOrder.setHistoryProductOrders(historyProductOrders);

    // sum list of discounted prices to set as totalPrice in database
    BigDecimal totalPriceFinal = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    customerOrder.setTotalPrice(totalPriceFinal);

    return customerOrderRepository.save(customerOrder);

  }

  public List<CustomerOrder> viewCustomerOrders(String customerId) {
    CustomerProfile customerProfile = customerProfileRepository.findById(customerId).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exists !"));

    return customerOrderRepository.findAllByCustomerProfile(customerProfile);
  }

  public CustomerOrder confirmPayOrder(String customerId, int estHour, int estMinute, int estSecond) {

    // get customer cart info
    CustomerCart customerCart = customerCartService.getCustomerCart(customerId);

    CustomerOrder customerOrder = customerOrderRepository.findByCustomerProfileAndStatusEquals(
      customerCart.getCustomerProfile(), "Waiting for payment").orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer order does not exists !"));

    // check if customer already paid
    if (!Objects.equals(customerOrder.getStatus(), "Waiting for payment")) {
      throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Customer already paid !");
    }

    LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

    CustomerOrder previousCustomerOrder = customerOrderRepository.findFirstByStatusNotAndDateCreatedBetweenOrderByDateCreatedDesc(
      "Waiting for payment", startOfDay, endOfDay).orElse(null);

    // set order number from previous order +1 if available
    if (previousCustomerOrder != null) {
      customerOrder.setNumber(previousCustomerOrder.getNumber() + 1);
    } else {
      customerOrder.setNumber(1);
    }

    // update status to processing order
    customerCart.setPayed(true);
    customerOrder.setStatus("Processing");
    customerCartRepository.save(customerCart);

    // add to waiting list in firebase
    Firestore firestore = FirestoreClient.getFirestore();
    DocumentReference documentReference = firestore.collection("Waiting_List").document();

    WaitingList waitingList = new WaitingList();
    waitingList.setId(documentReference.getId());
    waitingList.setCustomerName(customerCart.getCustomerProfile().getUsername());
    waitingList.setEstHour(estHour);
    waitingList.setEstMinute(estMinute);
    waitingList.setEstSecond(estSecond);

    int hourToSecond = (estHour * 60) * 60;
    int minuteToSecond = (estMinute * 60);
    int totalSecond = (1000 * (hourToSecond + minuteToSecond + estSecond));

    waitingList.setEstTime(totalSecond);

    ApiFuture<WriteResult> apiFuture = documentReference.set(waitingList);

    // delete or reset current customer cart
    // customerCartRepository.delete(customerCart);

    return customerOrderRepository.save(customerOrder);
  }

}
