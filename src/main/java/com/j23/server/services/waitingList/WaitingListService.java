package com.j23.server.services.waitingList;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.*;
import com.j23.server.models.note.Note;
import com.j23.server.models.waitingList.CountdownWaitingList;
import com.j23.server.models.waitingList.WaitingList;
import com.j23.server.services.customer.customerOrder.CustomerOrderService;
import com.j23.server.util.AppsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static com.j23.server.util.AppsConfig.frontEndUrl;
import static com.j23.server.util.AppsConfig.productionFrontEndUrl;


@Service
@Slf4j
public class WaitingListService {

  @Autowired
  private CustomerOrderService customerOrderService;

  @Autowired
  private WaitingListService waitingListService;

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

        // update status for current customer waiting list in firebase to WAITING FOR CONFIRMATION
        updateWaitingListStatus(waitingList, "WAITING FOR CONFIRMATION", 2);

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

  public void updateStatusToReadyToPickup(WaitingList waitingList) {

    // update status to waiting
    Firestore firestore = FirestoreClient.getFirestore();
    DocumentReference documentReference = firestore.collection("Waiting_List").document(waitingList.getId());
    Map<String, Object> updates = new HashMap<>();
    updates.put("status", "WAITING FOR PICKUP");
    updates.put("steps", 3);

    ApiFuture<WriteResult> writeResult = documentReference.update(updates);

    // send fcm notification for customer
    Note note = new Note();
    note.setUsername(waitingList.getUsername());
    note.setMessagingToken(waitingList.getMessagingToken());

    waitingListService.sendOrderDoneNotification(note);
  }

  // send push notification using fcm
  public Note sendOrderDoneNotification(Note note) {

    AndroidNotification androidNofi = AndroidNotification.builder()
      .setSound("default")
      .setPriority(AndroidNotification.Priority.HIGH)
      .setClickAction("ACTION_VIEW")
      .build();

    Aps aps = Aps.builder()
      .setSound("default")
      .build();

    ApnsConfig apnsConfig = ApnsConfig.builder()
      .setAps(aps)
      .setFcmOptions(
        ApnsFcmOptions
          .builder()
          .setImage("https://source.unsplash.com/user/c_v_r/1900x800")
          .build())
      .build();


    AndroidConfig androidConfig = AndroidConfig.builder()
      .setNotification(androidNofi)
      .setPriority(AndroidConfig.Priority.HIGH)
      .build();

    Notification notification = Notification
      .builder()
      .setTitle("Self Service")
      .setBody("Hello, " + note.getUsername() + "\r\n" + "Your order is ready to collect !")
      .build();

    Message.Builder builder = Message.builder()
      .setNotification(notification)
      .setApnsConfig(apnsConfig)
      .setAndroidConfig(androidConfig)
      .putData("priority", "high")
      .setWebpushConfig(WebpushConfig
        .builder()
        .setFcmOptions(
          WebpushFcmOptions // to redirect to url
            .builder()
            .setLink(productionFrontEndUrl + "#/order-success")
            .build())
        .setNotification(
          WebpushNotification
            .builder()
            .setIcon(AppsConfig.mainUrl + AppsConfig.appName
              + "icon/download/main") // notification icon
            .build())
        .build())
      .setToken(note.getMessagingToken());

//        if (note.getData() != null) builder.putAllData(note.getData());

    String response = null;
    try {
      response = FirebaseMessaging.getInstance().send(builder.build());
      log.trace(response);
      log.info("Message sent to token {}", note.getMessagingToken());
    } catch (FirebaseMessagingException e) {
      log.error("Fail to send firebase notification", e);
    }

    return note;

  }

}
