package com.j23.server.services.midtrans;

import com.midtrans.Midtrans;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MidTransExample {

  public static void main(String[] args) throws MidtransError {

    // Set serverKey to Midtrans global config
    Midtrans.serverKey = "SB-Mid-server-TTNlxI6thXHFeOeHRyj7dj_B";

    // Set value to true if you want Production Environment (accept real transaction).
    Midtrans.isProduction = false;

    // Create Token and then you can send token variable to FrontEnd,
    // to initialize Snap JS when customer click pay button
    String transactionToken = SnapApi.createTransactionToken(requestBody());
  }

  // Create params JSON Raw Object request
  public static Map<String, Object> requestBody() {
    UUID idRand = UUID.randomUUID();
    Map<String, Object> params = new HashMap<>();

    Map<String, String> transactionDetails = new HashMap<>();
    transactionDetails.put("order_id", String.valueOf(idRand));
    transactionDetails.put("gross_amount", "265000");

    Map<String, String> creditCard = new HashMap<>();
    creditCard.put("secure", "true");

    params.put("transaction_details", transactionDetails);
    params.put("credit_card", creditCard);

    return params;
  }
}
