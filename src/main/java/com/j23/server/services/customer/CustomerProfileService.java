package com.j23.server.services.customer;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerCart.CustomerCart;
import com.j23.server.repos.customer.CustomerProfileRepo;
import com.j23.server.services.customer.customerCart.CustomerCartService;
import com.j23.server.util.FooProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CustomerProfileService {

  @Autowired
  private CustomerProfileRepo customerProfileRepo;

  @Autowired
  private CustomerCartService customerCartService;

  private final FooProperties fooProperties;

  public CustomerProfileService(FooProperties fooProperties) {
    this.fooProperties = fooProperties;
  }

  public CustomerCart registerCustomer(CustomerProfile customerProfile) {
//    String ip = this.fooProperties.getIP();
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
      return customerCartService.createCart(customerProfileRepo.save(customerProfile));

    } catch (FirebaseAuthException e) {
      log.error("Firebase auth exception" + e.getAuthErrorCode() + "code for username" + customerProfile.getUsername());
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
    }
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

  public void updateMessagingToken(String messagingToken, String customerId) {
    // update in db
    customerProfileRepo.updateCustomerMessagingToken(messagingToken, customerId);

    // update messaging token in firebase
    Firestore firestore = FirestoreClient.getFirestore();
    DocumentReference documentReference = firestore.collection("Waiting_List").document(customerId);
    Map<String, Object> updates = new HashMap<>();
    updates.put("messagingToken", messagingToken);

    ApiFuture<WriteResult> writeResult = documentReference.update(updates);
    log.info("Customer messaging token has been updated!");
  }

  public Long getTotalCustomers() {
    return customerProfileRepo.count();
  }
}
