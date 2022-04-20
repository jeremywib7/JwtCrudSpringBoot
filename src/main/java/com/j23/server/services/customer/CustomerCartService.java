package com.j23.server.services.customer;

import com.google.common.collect.Iterables;
import com.j23.server.controllers.exception.ProductNotFoundException;
import com.j23.server.models.customer.CustomerCart;
import com.j23.server.models.customer.OrderedProduct;
import com.j23.server.models.product.Product;
import com.j23.server.repos.customer.CustomerCartRepository;
import com.j23.server.repos.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerCartService {

    @Autowired
    private CustomerCartRepository customerCartRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CustomerCart> findAllCustomerOrder() {
        return customerCartRepository.findAll();
    }

    public void createCart(String customerId) {
        CustomerCart customerCart = new CustomerCart();
        customerCart.setId(customerId);

        LocalDateTime localDateTime = LocalDateTime.now();
        customerCart.setDateCreated(LocalDateTime.from(localDateTime));

        customerCartRepository.save(customerCart);
    }

    public CustomerCart addProductToCart(String customerId, String productId, Integer productQuantity) {
        CustomerCart customerCart = customerCartRepository.findById(customerId).orElseThrow(() ->
                new ProductNotFoundException("Product not found"));

        // get product
        Product product = productRepository.findProductById(productId);

        // set into ordered product
        OrderedProduct orderedProduct = new OrderedProduct();
        orderedProduct.setId(String.valueOf(UUID.randomUUID()));
        orderedProduct.setProduct(product);
        orderedProduct.setQuantity(productQuantity);

        // add into current cart
        customerCart.getOrderedProduct().add(orderedProduct);

        // update time
        customerCart.setUpdatedOn(LocalDateTime.now());

        return customerCartRepository.save(customerCart);
    }

    public CustomerCart updateProductQuantityInCart(String customerId, String productId, Integer productQuantity) {
        CustomerCart customerCart = customerCartRepository.findById(customerId).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        // get index for update the index
        int index =
                Iterables.indexOf(customerCart.getOrderedProduct(), u -> u.getProduct().getId().equals(productId));

//        customerCart.getOrderedProduct().indexOf(index);

        // update time
        LocalDateTime localDateTime = LocalDateTime.now();
        customerCart.setUpdatedOn(LocalDateTime.from(localDateTime));

        return customerCart;
    }
}
