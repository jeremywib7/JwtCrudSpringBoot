package com.j23.server.services.waitingList;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.j23.server.models.waitingList.CountdownWaitingList;
import com.j23.server.models.waitingList.WaitingList;
import com.j23.server.services.customer.customerOrder.CustomerOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;


@Service
@Slf4j
public class WaitingListService {

  @Autowired
  private CustomerOrderService customerOrderService;

  public List<CountdownWaitingList> countdownWaitingLists = new ArrayList<>();

  public void addToCountdownWaitingList(WaitingList waitingList) {
    log.trace("Executing method addToCountdownWaitingList");

    LocalDateTime estTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(waitingList.getEstTime()), TimeZone.getDefault().toZoneId());

    Calendar date = Calendar.getInstance();
    date.set(Calendar.YEAR, estTime.getYear());
    date.set(Calendar.MONTH, estTime.getMonthValue() - 1);
    date.set(Calendar.DAY_OF_MONTH, estTime.getDayOfMonth());
    date.set(Calendar.HOUR_OF_DAY, estTime.getHour());
    date.set(Calendar.MINUTE, estTime.getMinute());
    date.set(Calendar.SECOND, estTime.getSecond());

    CountdownWaitingList countdownWaitingList = new CountdownWaitingList();
    countdownWaitingList.setCustomerId(waitingList.getId());
    TimerTask task = new TimerTask() {
      public void run() {
        log.trace("Executing timer task for firebase...");
        log.info("Updating waiting list status for {}", waitingList.getId());

        // update status in database
        customerOrderService.updateOrderStatus(waitingList.getId(), "Waiting");

        // update status for current customer waiting list in firebase to WAITING
        updateWaitingListStatus(waitingList, "WAITING", 2);

        // delete from array
        boolean isRemoved = countdownWaitingLists.removeIf(wl -> wl.getCustomerId().equals(waitingList.getId()));
        if (isRemoved) {
          log.info("Success clear timer");
        } else {
          log.error("Failed clear timer");
        }

        log.info("Current list : {}", (long) countdownWaitingLists.size());

      }
    };
    countdownWaitingList.setOnFinished(task);
    countdownWaitingList.getEstimatedTime().schedule(countdownWaitingList.getOnFinished(), date.getTime());
    countdownWaitingLists.add(countdownWaitingList); // add to list of countdown waiting list timer

  }

  public void updateWaitingListStatus(WaitingList waitingList, String status, int steps) {
    // update status to waiting
    Firestore firestore = FirestoreClient.getFirestore();
    DocumentReference documentReference = firestore.collection("Waiting_List").document(waitingList.getId());
    Map<String, Object> updates = new HashMap<>();
    updates.put("status", status);
    updates.put("steps", steps);

    ApiFuture<WriteResult> writeResult = documentReference.update(updates);
  }

}
