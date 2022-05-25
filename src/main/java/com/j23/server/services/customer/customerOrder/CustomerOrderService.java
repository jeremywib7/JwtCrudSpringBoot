package com.j23.server.services.customer.customerOrder;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
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
import com.j23.server.services.waitingList.WaitingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
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
  private WaitingListService waitingListService;

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
    customerOrder.setOrderIsActive(true);
    customerOrder.setTotalPrice(totalPrice);
    customerOrder.setHistoryProductOrders(historyProductOrders);

    // sum list of discounted prices to set as totalPrice in database
    BigDecimal totalPriceFinal = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    customerOrder.setTotalPrice(totalPriceFinal);

    return customerOrderRepository.save(customerOrder);

  }

  public List<CustomerOrder> viewCustomerOrdersByCustomerId(String customerId) {
    CustomerProfile customerProfile = customerProfileRepository.findById(customerId).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exists !"));

    return customerOrderRepository.findAllByCustomerProfileOrderByDateCreatedDesc(customerProfile);
  }

  public CustomerOrder viewCustomerOrderByCustomerUsername(String username) {
    CustomerProfile customerProfile = customerProfileRepository.findByUsername(username).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exists !"));

    return customerOrderRepository.findTopByCustomerProfileAndOrderProcessedIsNullOrderByDateCreatedDesc(customerProfile)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer order does not exists"));
  }

  public CustomerOrder viewCurrentCustomerOrder(String customerId) {
    CustomerProfile customerProfile = customerProfileRepository.findById(customerId).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exists !"));

    return customerOrderRepository.findByCustomerProfileAndOrderIsActiveTrue(customerProfile).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer order does not exists"));
  }

  public CustomerOrder confirmPayOrder(CustomerOrder customerOrder) {

    // get customer cart and customer order info
    // check if customer order exists
    CustomerCart customerCart = customerCartService.getCustomerCart(customerOrder.getCustomerProfile().getId());
    CustomerOrder currentCustomerOrder = customerOrderRepository.findTopByCustomerProfileAndOrderIsActiveTrueOrderByDateCreatedDesc(
      customerCart.getCustomerProfile()).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer order does not exists !"));

    // check if customer already paid, by is order already active
    // order is not active if customer haven't paid or order already finished
    if (customerCart.isPaid()) {
      throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Customer already paid !");
    }

    LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    CustomerOrder previousCustomerOrder = customerOrderRepository
      .findFirstByOrderProcessedIsNotNullAndDateCreatedBetweenOrderByDateCreatedDesc(
      startOfDay, endOfDay).orElse(null);


    int currentNumber;
    // set order number from previous order +1 if available
    if (previousCustomerOrder != null) {
      currentNumber = previousCustomerOrder.getNumber() + 1;
    } else {
      currentNumber = 1;
    }


    // update customer order
    // calculate estimated time
    int hourToSecond = (customerOrder.getEstHour() * 60) * 60;
    int minuteToSecond = (customerOrder.getEstMinute() * 60);
    long addedTime = new Date().getTime() + (1000L * (hourToSecond + minuteToSecond + customerOrder.getEstSecond()));

    // set estimated time in dd/mm/yyyy format to be saved in database
    LocalDateTime estTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(addedTime), TimeZone.getDefault().toZoneId());
    customerOrder.setId(currentCustomerOrder.getId());
    customerOrder.setEstTime(estTime);
    customerOrder.setOrderIsActive(currentCustomerOrder.isOrderIsActive());
    customerOrder.setHistoryProductOrders(currentCustomerOrder.getHistoryProductOrders());
    customerOrder.setNumber(currentNumber);
    customerOrder.setOrderProcessed(LocalDateTime.now());

    // add waiting list data to firebase
    WaitingList waitingList = new WaitingList();
    waitingList.setId(customerCart.getCustomerProfile().getId());
    waitingList.setUsername(customerCart.getCustomerProfile().getUsername());
    waitingList.setMessagingToken(customerCart.getCustomerProfile().getMessagingToken());
    waitingList.setEstTime(addedTime);
    waitingList.setNumber(currentNumber);

    // add to waiting list in firebase
    Firestore firestore = FirestoreClient.getFirestore();
    DocumentReference documentReference = firestore.collection("Waiting_List").document(
      customerOrder.getCustomerProfile().getId());
    ApiFuture<WriteResult> apiFuture = documentReference.set(waitingList);

    // add to countdown waiting list array to update from server side
    waitingListService.addToCountdownWaitingList(waitingList);

    // update status to processing order and set order to be active
    customerCart.setPaid(true);
    customerCartRepository.save(customerCart);

    return customerOrderRepository.save(customerOrder);
  }

  public void updateOrderStatus(String customerId, String status) {
    // get customer cart info
    CustomerCart customerCart = customerCartService.getCustomerCart(customerId);

    log.info("Updating order status for id {}", customerId);

//    customerOrderRepository.save(customerOrder);
  }

  public void finishOrder(String customerId) {
    // get customer cart info
    CustomerCart customerCart = customerCartService.getCustomerCart(customerId);

//    CustomerOrder customerOrder = customerOrderRepository.findTopByCustomerProfileAndStatusEqualsOrderByDateCreatedDesc(
//      customerCart.getCustomerProfile(), "Waiting").orElseThrow(() ->
//      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer order does not exists !"));
//    customerOrder.setStatus("Completed");
//
//    customerOrderRepository.save(customerOrder);

    // delete or reset current customer cart
    customerCartRepository.delete(customerCart);

  }

}
