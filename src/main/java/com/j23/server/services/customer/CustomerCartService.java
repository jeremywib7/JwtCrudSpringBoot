package com.j23.server.services.customer;

import com.google.common.collect.Iterables;
import com.j23.server.models.customer.CustomerCart;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.OrderedProduct;
import com.j23.server.models.product.Product;
import com.j23.server.repos.customer.CustomerCartRepository;
import com.j23.server.repos.customer.CustomerProfileRepo;
import com.j23.server.repos.customer.OrderedProductRepo;
import com.j23.server.repos.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerCartService {

  @Autowired
  private CustomerCartRepository customerCartRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CustomerProfileRepo customerProfileRepo;

  @Autowired
  private OrderedProductRepo orderedProductRepo;

  public List<CustomerCart> findAllCustomerOrder() {
    return customerCartRepository.findAll();
  }

  public CustomerCart createCart(CustomerProfile customerProfile) {
    LocalDateTime localDateTime = LocalDateTime.now();

    CustomerCart customerCart = new CustomerCart();
    customerCart.setId(customerProfile.getId());
    customerCart.setCustomerDetail(customerProfile);
    customerCart.setOrderedProduct(new ArrayList<>());
    customerCart.setDateCreated(LocalDateTime.from(localDateTime));

    return customerCartRepository.save(customerCart);
  }

//  public CustomerCart findCustomerCart(String id) {
//
//  }

  public CustomerCart updateCart(String customerId, String productId, Integer productQuantity) {

    // check if customer cart exists or throw exception if not found
    CustomerProfile customerProfile = customerProfileRepo.findById(customerId).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer is not found !"));
    CustomerCart customerCart = customerCartRepository.findById(customerId).orElse(createCart(customerProfile));

    // get product detail or throw exception if product not found
    Product product = productRepository.findById(productId).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found !"));

    // find all ordered product for current customer

    // set into ordered product
    OrderedProduct orderedProduct = new OrderedProduct();
    // set id format (CustomerName_ProductId)
    orderedProduct.setId(String.valueOf(UUID.randomUUID())); // dont set for random UUID
    orderedProduct.setProduct(product);
    orderedProduct.setQuantity(productQuantity);

    // add into current cart
    customerCart.getOrderedProduct().add(orderedProduct);

    // int index = Iterables.indexOf(customerCart.getOrderedProduct(), u -> u.getProduct().getId().equals(productId));

    // update time
    customerCart.setUpdatedOn(LocalDateTime.now());

    return customerCartRepository.save(customerCart);
  }

//  public CustomerCart updateProductQuantityInCart(String customerId, String productId, Integer productQuantity) {
//    CustomerCart customerCart = customerCartRepository.findById(customerId).orElseThrow(() ->
//      new UsernameNotFoundException("User not found"));
//
//    // get index for update the index
//    int index = Iterables.indexOf(customerCart.getOrderedProduct(), u -> u.getProduct().getId().equals(productId));
//
////        customerCart.getOrderedProduct().indexOf(index);
//
//    // update time
//    LocalDateTime localDateTime = LocalDateTime.now();
//    customerCart.setUpdatedOn(LocalDateTime.from(localDateTime));
//
//    return customerCart;
//  }
}
