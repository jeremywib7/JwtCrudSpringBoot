package com.j23.server.services.customer.customerCart;

import com.j23.server.models.customer.customerCart.CustomerCart;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerCart.CartOrderedProduct;
import com.j23.server.models.product.Product;
import com.j23.server.repos.customer.customerCart.CustomerCartRepository;
import com.j23.server.repos.customer.CustomerProfileRepo;
import com.j23.server.repos.customer.customerCart.CartOrderedProductRepo;
import com.j23.server.repos.product.ProductRepository;
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
  private CustomerProfileRepo customerProfileRepo;

  @Autowired
  private CartOrderedProductRepo cartOrderedProductRepo;

  public CustomerCart getCustomerCart(String customerId) {
    CustomerProfile customerProfile = customerProfileRepo.findById(customerId).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exists !"));

    return customerCartRepository.findByCustomerProfile(customerProfile).orElseGet(() -> createCart(customerProfile));
  }

  public CartOrderedProduct getOrderedProduct(List<CartOrderedProduct> cartOrderedProductList, String productIdToSearch) {
    return cartOrderedProductList.stream()
      .filter(orderedProduct1 -> productIdToSearch.equals(orderedProduct1.getProduct().getId()))
      .findAny()
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product does not exists !"));
  }

  public CustomerCart createCart(CustomerProfile customerProfile) {

    CustomerCart customerCart = new CustomerCart();
    customerCart.setId(String.valueOf(UUID.randomUUID()));
    customerCart.setCustomerProfile(customerProfile);

    return customerCartRepository.save(customerCart);
  }

  public CustomerCart viewCart(String customerId) {
    return getCustomerCart(customerId);
  }

  public CartOrderedProduct addProductToCart(String customerId, String productId, Integer productQuantity) {

    // check if customer cart exists or throw exception if not found
    CustomerCart customerCart = getCustomerCart(customerId);

    // get product detail or throw exception if product not found
    Product product = new Product();
    product.setId(productId);

    CartOrderedProduct cartOrderedProduct = new CartOrderedProduct();
    cartOrderedProduct.setId(String.valueOf(UUID.randomUUID()));
    cartOrderedProduct.setProduct(product);
    cartOrderedProduct.setQuantity(productQuantity);
    cartOrderedProduct = cartOrderedProductRepo.save(cartOrderedProduct);

    customerCart.setUpdatedOn(LocalDateTime.now());
    customerCart.getCartOrderedProduct().add(cartOrderedProduct);
    customerCartRepository.save(customerCart);

    return cartOrderedProduct;
  }


  public CustomerCart updateProductQuantityInCart(String customerId, String productId, Integer productQuantity) {
    LocalDateTime currentTime = LocalDateTime.now();

    // check if customer cart exists or throw exception if not found
    CustomerCart customerCart = getCustomerCart(customerId);

    // find product to be updated
    CartOrderedProduct cartOrderedProduct = getOrderedProduct(customerCart.getCartOrderedProduct(), productId);

    assert cartOrderedProduct != null;
    cartOrderedProduct.setQuantity(productQuantity);

    customerCart.setUpdatedOn(currentTime);

    return customerCartRepository.save(customerCart);
  }

  public CustomerCart removeProductFromCart(String customerId, String productId) {
    CustomerCart customerCart = getCustomerCart(customerId);

    // find product to be updated
    CartOrderedProduct cartOrderedProduct = customerCart.getCartOrderedProduct().stream()
      .filter(orderedProduct1 -> productId.equals(orderedProduct1.getProduct().getId()))
      .findAny()
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product does not exists !"));

    cartOrderedProductRepo.delete(cartOrderedProduct);
    customerCart.getCartOrderedProduct().remove(cartOrderedProduct);

    return customerCart;
  }

  // find index to update to
//    int index = customerCart.getOrderedProduct().indexOf(orderedProduct);
//    customerCart.getOrderedProduct().set(index, orderedProduct); // update index
}
