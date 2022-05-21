package com.j23.server.services.waitingList;

import com.google.api.core.ApiFuture;
import com.google.cloud.Condition;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.*;
import com.j23.server.models.note.Note;
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
                customerOrderService.updateOrderStatus(waitingList.getId(), "Processing");

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

    // send push notification using fcm
    public String sendPushNotification(Note note, String token) {

        // to display notification
        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setImage(note.getImage())
                .setBody(note.getContent())
                .build();

        // set link to redirect url to website
        WebpushFcmOptions webpushFcmOptions = WebpushFcmOptions
                .builder()
                .setLink(note.getLink())
                .build();

        // change icon
        WebpushNotification webpushNotification = WebpushNotification
                .builder()
                .setIcon(null)
                .build();

//        ApnsConfig apnsFcmOptions = ApnsConfig
//                .builder()
//                .setAps(Aps.builder().setCategory("5").build()).build();

        // to redirect website
        WebpushConfig webpushConfig = WebpushConfig
                .builder()
                .setFcmOptions(webpushFcmOptions)
                .setNotification(webpushNotification)
                .build();

        // set message
        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .setWebpushConfig(webpushConfig)
                .putAllData(note.getData())
//                .setApnsConfig(apnsFcmOptions)
                .build();

        String response = null;
        try {
            response = FirebaseMessaging.getInstance().send(message);
            log.info("Message sent to token {}", token);
        } catch (FirebaseMessagingException e) {
            log.error("Fail to send firebase notification", e);
        }

        return response;
    }

}
