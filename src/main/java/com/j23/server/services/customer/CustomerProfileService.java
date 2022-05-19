package com.j23.server.services.customer;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerCart.CustomerCart;
import com.j23.server.repos.customer.CustomerProfileRepo;
import com.j23.server.services.customer.customerCart.CustomerCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class CustomerProfileService {

  @Autowired
  private CustomerProfileRepo customerProfileRepo;

  @Autowired
  private CustomerCartService customerCartService;

  public CustomerCart registerCustomer(CustomerProfile customerProfile) {

    if (customerProfileRepo.existsByUsername(customerProfile.getUsername())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists !");
    }

    // save user auth in firebase
    UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest();
    createRequest.setEmail(customerProfile.getEmail());
    createRequest.setDisplayName(customerProfile.getUsername());
    createRequest.setPassword(customerProfile.getPassword());

    try {
      UserRecord userRecord = FirebaseAuth.getInstance().createUser(createRequest);

      // get uid from firebase and set in user record
      customerProfile.setId(userRecord.getUid());
    } catch (FirebaseAuthException e) {
      log.error("Firebase auth exception"+ e.getAuthErrorCode() + "code for username" + customerProfile.getUsername());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    return customerCartService.createCart(customerProfileRepo.save(customerProfile));

  }

  public CustomerProfile getCustomerById(String customerId) {
    return customerProfileRepo.findById(customerId).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found !"));
  }

  public CustomerProfile getCustomerByUsername(String username) {
    return customerProfileRepo.findByUsername(username).orElseThrow(() ->
      new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found !"));
  }

  public CustomerProfile saveCustomerProfile(CustomerProfile customerProfile) {
    return customerProfileRepo.save(customerProfile);
  }
}
