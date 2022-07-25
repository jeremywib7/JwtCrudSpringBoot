package com.j23.server.services.midtrans;

import com.j23.server.models.customer.customerCart.CustomerCart;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.repos.customer.customerOrder.CustomerOrderRepository;
import com.j23.server.services.customer.customerCart.CustomerCartService;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MidtransService {

    private final CustomerCartService customerCartService;
    private final CustomerOrderRepository customerOrderRepository;

    public Map<String, Object> createSnapToken(CustomerOrder customerOrder) throws MidtransError {
        // get customer cart and customer order info
        // check if customer order exists
        CustomerCart customerCart = customerCartService.getCustomerCart(customerOrder.getCustomerProfile().getId());
        CustomerOrder currentCustomerOrder = customerOrderRepository.findTopByCustomerProfileAndOrderIsActiveTrueOrderByDateTimeCreatedDesc(
                customerCart.getCustomerProfile()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer order does not exists !"));

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Map<String, Object> transDetail = new HashMap<>();
        transDetail.put("order_id", "SELF_SERVICE_" + timestamp.getTime());
        transDetail.put("gross_amount", customerOrder.getTotalPrice());

        List<Map<String, Object>> items = new ArrayList<>();
        currentCustomerOrder.getHistoryProductOrders().forEach(historyProductOrder -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", UUID.randomUUID().toString());
            item.put("price", historyProductOrder.getDiscountedPrice().toString());
            item.put("quantity", historyProductOrder.getQuantity());
            item.put("name", historyProductOrder.getName());
            items.add(item);
        });

        Map<String, Object> billingAddres = new HashMap<>();
        billingAddres.put("first_name", currentCustomerOrder.getCustomerProfile().getFirstName());
        billingAddres.put("last_name", currentCustomerOrder.getCustomerProfile().getLastName());
        billingAddres.put("email", currentCustomerOrder.getCustomerProfile().getEmail());
//        billingAddres.put("phone", "0928282828");
//        billingAddres.put("address", currentCustomerOrder.getCustomerProfile().get());
//        billingAddres.put("city", "Jakarta Selatan");
//        billingAddres.put("postal_code", "10120");
//        billingAddres.put("country_code", "IDN");

        Map<String, Object> custDetail = new HashMap<>();
        custDetail.put("first_name", currentCustomerOrder.getCustomerProfile().getFirstName());
        custDetail.put("last_name", currentCustomerOrder.getCustomerProfile().getLastName());
        custDetail.put("email",  currentCustomerOrder.getCustomerProfile().getEmail());
//        custDetail.put("phone", currentCustomerOrder.getCustomerProfile().get);
//        custDetail.put("billing_address", billingAddres);

        Map<String, Object> body = new HashMap<>();
        body.put("transaction_details", transDetail);
        body.put("item_details", items);
        body.put("customer_details", custDetail);

        String transactionToken = SnapApi.createTransactionToken(body);

        Map<String, Object> token = new LinkedHashMap<>();
        token.put("snap_token", transactionToken);
        return token;
    }

}
