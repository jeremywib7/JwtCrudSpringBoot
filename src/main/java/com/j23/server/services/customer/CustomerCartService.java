package com.j23.server.services.customer;

import com.j23.server.models.customer.CustomerCart;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.OrderedProduct;
import com.j23.server.models.product.Product;
import com.j23.server.repos.customer.CustomerCartRepository;
import com.j23.server.repos.customer.CustomerProfileRepo;
import com.j23.server.repos.customer.OrderedProductRepo;
import com.j23.server.repos.product.ProductRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

  public CustomerCart getCustomerCart(String customerId) {
    CustomerProfile customerProfile = customerProfileRepo.findById(customerId).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exists !"));

    return customerCartRepository.findByCustomerProfile(customerProfile).orElseGet(() -> createCart(customerProfile));
  }

  public OrderedProduct getOrderedProduct(List<OrderedProduct> orderedProductList, String productIdToSearch) {
    return orderedProductList.stream()
      .filter(orderedProduct1 -> productIdToSearch.equals(orderedProduct1.getProduct().getId()))
      .findAny()
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product does not exists !"));
  }

  public CustomerCart createCart(CustomerProfile customerProfile) {
    LocalDateTime localDateTime = LocalDateTime.now();

    CustomerCart customerCart = new CustomerCart();
    customerCart.setId(String.valueOf(UUID.randomUUID()));
    customerCart.setCustomerProfile(customerProfile);
    customerCart.setOrderedProduct(new ArrayList<>());
    customerCart.setDateCreated(LocalDateTime.from(localDateTime));

    return customerCartRepository.save(customerCart);
  }

  public CustomerCart viewCart(String customerId) {
    return getCustomerCart(customerId);
  }

  public OrderedProduct addProductToCart(String customerId, String productId, Integer productQuantity) {

    // check if customer cart exists or throw exception if not found
    CustomerCart customerCart = getCustomerCart(customerId);

    // get product detail or throw exception if product not found
    Product product = new Product();
    product.setId(productId);

    OrderedProduct orderedProduct = new OrderedProduct();
    orderedProduct.setId(String.valueOf(UUID.randomUUID()));
    orderedProduct.setProduct(product);
    orderedProduct.setQuantity(productQuantity);
    orderedProduct.setCreatedOn(LocalDateTime.now());
    orderedProduct.setUpdatedOn(LocalDateTime.now());
    orderedProductRepo.save(orderedProduct);

    customerCart.setUpdatedOn(LocalDateTime.now());
    customerCart.getOrderedProduct().add(orderedProduct);
    customerCartRepository.save(customerCart);

    return orderedProduct;
  }

  public CustomerCart updateProductQuantityInCart(String customerId, String productId, Integer productQuantity) {
    LocalDateTime currentTime = LocalDateTime.now();

    // check if customer cart exists or throw exception if not found
    CustomerCart customerCart = getCustomerCart(customerId);

    // find product to be updated
    OrderedProduct orderedProduct = getOrderedProduct(customerCart.getOrderedProduct(), productId);

    assert orderedProduct != null;
    orderedProduct.setQuantity(productQuantity);
    orderedProduct.setUpdatedOn(currentTime);

    customerCart.setUpdatedOn(currentTime);

    return customerCartRepository.save(customerCart);
  }

  public CustomerCart removeProductFromCart(String customerId, String productId) {
    CustomerCart customerCart = getCustomerCart(customerId);

    // find product to be updated
    OrderedProduct orderedProduct = customerCart.getOrderedProduct().stream()
      .filter(orderedProduct1 -> productId.equals(orderedProduct1.getProduct().getId()))
      .findAny()
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product does not exists !"));

    orderedProductRepo.delete(orderedProduct);
    customerCart.getOrderedProduct().remove(orderedProduct);

    return customerCart;
  }

  // find index to update to
//    int index = customerCart.getOrderedProduct().indexOf(orderedProduct);
//    customerCart.getOrderedProduct().set(index, orderedProduct); // update index
}
