package com.j23.server.services.waitingList;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.j23.server.models.waitingList.CountdownWaitingList;
import com.j23.server.models.waitingList.WaitingList;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class WaitingListService {

  public List<CountdownWaitingList> countdownWaitingLists = new ArrayList<>();

  public void addToCountdownWaitingList(WaitingList waitingList) {

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
        System.out.println("Updating firebase status !!");

        // update status for current customer waiting list in firebase to WAITING
        updateWaitingListStatus(waitingList, "WAITING", 2);

        // delete from array
        // countdownWaitingLists.removeIf(wl -> wl.getCustomerId().equals(waitingList.getId()));
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
