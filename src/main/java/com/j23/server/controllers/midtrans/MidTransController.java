package com.j23.server.controllers.midtrans;

import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.services.midtrans.MidtransService;
import com.midtrans.httpclient.error.MidtransError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MidTransController {
    private final MidtransService midtransService;

    @PostMapping(value = "/charge", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> chargePayment(@RequestBody CustomerOrder customerOrder) throws MidtransError {
        Map<String, Object> snapToken = midtransService.createSnapToken(customerOrder);
        return new ResponseEntity<>(snapToken, HttpStatus.OK);
    }

}
