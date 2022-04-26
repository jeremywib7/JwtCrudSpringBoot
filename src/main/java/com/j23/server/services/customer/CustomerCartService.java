package com.j23.server.services.customer;

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

    public CustomerProfile getCustomerProfile(String customerId) {
        return customerProfileRepo.findById(customerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer does not exists !"));
    }

    public CustomerCart getCustomerCart(CustomerProfile customerProfile) {
        return customerCartRepository.findByCustomerProfile(customerProfile)
                .orElseGet(() -> createCart(customerProfile));
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
        CustomerProfile customerProfile = getCustomerProfile(customerId);

        CustomerCart customerCart = getCustomerCart(customerProfile);

        List<OrderedProduct> orderedProductList = orderedProductRepo.findByCustomerCart(customerCart);
        customerCart.setOrderedProduct(orderedProductList);

        return customerCart;
    }

    public CustomerCart updateCart(String customerId, String productId, Integer productQuantity) {

        // check if customer cart exists or throw exception if not found

        CustomerProfile customerProfile = getCustomerProfile(customerId);

        CustomerCart customerCart = getCustomerCart(customerProfile);
        customerCart.setUpdatedOn(LocalDateTime.now());

        // get product detail or throw exception if product not found
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not exists !"));

        OrderedProduct orderedProduct = new OrderedProduct();
        orderedProduct.setId(customerId + "_" + productId);
        orderedProduct.setProduct(product);
        orderedProduct.setQuantity(productQuantity);
        orderedProduct.setCustomerCart(customerCart);
        orderedProductRepo.save(orderedProduct);

        List<OrderedProduct> orderedProductList = orderedProductRepo.findByCustomerCart(customerCart);
        customerCart.setOrderedProduct(orderedProductList);

        return customerCart;
    }

    public CustomerCart removeProductFromCart(String customerId, String productId) {
        // delete from database
        orderedProductRepo.deleteById(customerId + "_" + productId);

        CustomerProfile customerProfile = getCustomerProfile(customerId);

        CustomerCart customerCart = getCustomerCart(customerProfile);
        customerCart.setUpdatedOn(LocalDateTime.now());

        List<OrderedProduct> orderedProductList = orderedProductRepo.findByCustomerCart(customerCart);
        customerCart.setOrderedProduct(orderedProductList);

        return customerCartRepository.findByCustomerProfile(customerProfile).orElse(null);
    }
}
