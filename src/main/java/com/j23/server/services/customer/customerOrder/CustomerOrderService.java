package com.j23.server.services.customer.customerOrder;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.j23.server.models.customer.customerCart.CustomerCart;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.models.customer.customerOrder.HistoryProductOrder;
import com.j23.server.models.product.Product;
import com.j23.server.models.waitingList.WaitingList;
import com.j23.server.repos.customer.CustomerProfileRepo;
import com.j23.server.repos.customer.customerCart.CustomerCartRepository;
import com.j23.server.repos.customer.customerOrder.CustomerOrderRepository;
import com.j23.server.repos.customer.customerOrder.HistoryProductOrderRepo;
import com.j23.server.services.customer.customerCart.CustomerCartService;
import com.j23.server.services.dashboard.TotalSalesProductService;
import com.j23.server.services.time.TimeService;
import com.j23.server.services.waitingList.WaitingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

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

  @Autowired
  private TotalSalesProductService totalSalesProductService;

  @Autowired
  private TimeService timeService;

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
    List<BigDecimal> prices = new ArrayList<>();

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

  public Page<CustomerOrder> viewCustomerOrdersBetweenDateByCustomerId(String customerId, int page, int size) {
    CustomerProfile customerProfile = customerProfileRepository.findById(customerId).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exists !"));

    return customerOrderRepository.findCustomerOrdersByCustomerProfileAndDateTimeCreatedBetweenOrderByDateTimeCreatedDesc(
      customerProfile, timeService.getStartDateTimeOfCurrentMonth(), timeService.getEndDateTimeOfCurrentMonth(),
      PageRequest.of(page, size));
  }

//  public Page<CustomerOrder> viewCustomerOrdersByCustomerId(String customerId) {
//    CustomerProfile customerProfile = customerProfileRepository.findById(customerId).orElseThrow(() ->
//      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exists !"));
//
//    return customerOrderRepository.findCustomerOrdersByCustomerProfileOrderByDateTimeCreatedDesc(customerProfile);
//  }

  public List<CustomerOrder> viewCustomerOrdersByDateFilterAndCustomerId(String customerId, Pageable pageable) {

    CustomerProfile customerProfile = customerProfileRepository.findById(customerId).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exists !"));

    return customerOrderRepository.findAllByCustomerProfileOrderByDateTimeCreatedDesc(customerProfile);
  }

  public CustomerOrder viewCustomerOrderByCustomerUsername(String username) {
    CustomerProfile customerProfile = customerProfileRepository.findByUsername(username).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exists !"));

    return customerOrderRepository.findTopByCustomerProfileAndOrderProcessedIsNullOrderByDateTimeCreatedDesc(customerProfile)
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
    CustomerOrder currentCustomerOrder = customerOrderRepository.findTopByCustomerProfileAndOrderIsActiveTrueOrderByDateTimeCreatedDesc(
      customerCart.getCustomerProfile()).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer order does not exists !"));

    // check if customer already paid, by is order already active
    // order is not active if customer haven't paid or order already finished
    if (customerCart.isPaid()) {
      throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Customer already paid !");
    }

    // check number from previous customer order
    int currentNumber = checkNumberFromPreviousOrderOfTheDay();

    // update customer order
    // calculate estimated time
    long addedTime = calculateEstimatedTime(customerOrder.getEstHour(), customerOrder.getEstMinute(), customerOrder.getEstSecond());

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

  public void finishOrder(String customerId) {
    // get customer cart info
    CustomerCart customerCart = customerCartService.getCustomerCart(customerId);

    CustomerOrder customerOrder = customerOrderRepository.findTopByCustomerProfileAndOrderIsActiveTrueOrderByDateTimeCreatedDesc(
      customerCart.getCustomerProfile()).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer order does not exists !"));
    customerOrder.setOrderFinished(LocalDateTime.now());
    customerOrder.setOrderIsActive(false);
    customerOrderRepository.save(customerOrder);

    log.info("The cart : " + customerCart.getCartOrderedProduct());

    // add to history total sales product
    customerCart.getCartOrderedProduct().stream().map(cartOrderedProduct ->
      totalSalesProductService.sumProductProfit(cartOrderedProduct.getProduct(), customerOrder.getTotalPrice())).collect(
      Collectors.toSet());

    // delete or reset current customer cart
    customerCartRepository.delete(customerCart);

    // remove from waiting list in firebase
    Firestore firestore = FirestoreClient.getFirestore();
    DocumentReference documentReference = firestore.collection("Waiting_List").document(
      customerOrder.getCustomerProfile().getId());
    ApiFuture<WriteResult> apiFuture = documentReference.delete();

  }

  public void cancelOrder(String customerId) {
    // get customer cart info
    CustomerCart customerCart = customerCartService.getCustomerCart(customerId);

    CustomerOrder customerOrder = customerOrderRepository.findTopByCustomerProfileAndOrderIsActiveTrueOrderByDateTimeCreatedDesc(
      customerCart.getCustomerProfile()).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer order does not exists !"));
    customerOrder.setOrderCancelled(LocalDateTime.now());
    customerOrderRepository.save(customerOrder);

    // delete or reset current customer cart
    customerCartRepository.delete(customerCart);
  }

  public long calculateEstimatedTime(int estHour, int estMinute, int estSecond) {
    // calculate estimated time
    int hourToSecond = (estHour * 60) * 60;
    int minuteToSecond = (estMinute * 60);

    return new Date().getTime() + (1000L * (hourToSecond + minuteToSecond + estSecond));
  }

  public int checkNumberFromPreviousOrderOfTheDay() {
    CustomerOrder previousCustomerOrder = customerOrderRepository
      .findFirstByOrderProcessedIsNotNullAndDateTimeCreatedBetweenOrderByDateTimeCreatedDesc(
        timeService.getStartOfTheDay(), timeService.getEndOfTheDay()).orElse(null);

    int currentNumber;
    // set order number from previous order +1 if available
    if (previousCustomerOrder != null) {
      currentNumber = previousCustomerOrder.getNumber() + 1;
    } else {
      currentNumber = 1;
    }

    return currentNumber;
  }

  public List<CustomerOrder> viewRecentSales() {
    return customerOrderRepository.findAllByOrderFinishedIsNotNullAndOrderFinishedBetweenOrderByOrderFinishedDesc(
      timeService.getPrevious3Hours(), timeService.getCurrentHour());
  }

  public long getTotalOrdersForCurrentMonth() {
    return customerOrderRepository.countAllByOrderFinishedBetweenOrderByOrderFinishedDesc(
      timeService.getStartDateTimeOfCurrentMonth(), timeService.getEndDateTimeOfCurrentMonth());
  }

  public BigDecimal getTotalRevenue() {
    return customerOrderRepository.totalReveneu(timeService.getStartDateTimeOfCurrentMonth(),
      timeService.getEndDateTimeOfCurrentMonth());
  }

}
