package com.j23.server.controllers.midtrans;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.j23.server.util.AppsConfig.*;

@RestController
@RequiredArgsConstructor
public class MidTransController {
  Config configOptions = Config.builder()
    .enableLog(true)
    .setIsProduction(IS_PRODUCTION)
    .setServerKey(sandboxServerKey)
    .setClientKey(sandboxClientKey)
    .build();

  private MidtransCoreApi coreApi = new ConfigFactory(configOptions).getCoreApi();
  private final DataMockup dataMockup;

  // Core API Controller for fetch Gopay transaction
  @PostMapping(value = "/gopay/charge", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> goPay() throws MidtransError {
    dataMockup.setPaymentType("gopay");

    Map<String, Object> body = new HashMap<>(dataMockup.initDataMock());
    String transactionToken = SnapApi.createTransactionToken(body);
    System.out.println("THE TOKEN");
//        coreApi.apiConfig().paymentOverrideNotification("http://midtrans-java.herokuapp.com/notif/override1,http://midtrans-java.herokuapp.com/notif/override2");
//        JSONObject object = coreApi.chargeTransaction(body);
//    String result = object.toString();
    return new ResponseEntity<>(transactionToken, HttpStatus.OK);
  }

}
