package com.j23.server.controllers.waitingList;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.j23.server.models.waitingList.CountdownWaitingList;
import com.j23.server.models.waitingList.WaitingList;
import com.j23.server.services.waitingList.WaitingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/waitingList")
public class WaitingListController {

  @Autowired
  private WaitingListService waitingListService;

  @PostConstruct
  public void getAllTimerList() throws ExecutionException, InterruptedException {
    List<CountdownWaitingList> countdownWaitingLists = new ArrayList<>();

    // get all waiting list and set countdown
    Firestore firestore = FirestoreClient.getFirestore();

    ApiFuture<QuerySnapshot> apiFuture = firestore.collection("Waiting_List").get();
    List<QueryDocumentSnapshot> list = apiFuture.get().getDocuments();
    List<WaitingList> waitingListList = list.stream().map((doc) -> doc.toObject(WaitingList.class)).collect(Collectors.toList());

    waitingListList.forEach(waitingList -> {
      LocalDateTime estTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(waitingList.getEstTime()), TimeZone.getDefault().toZoneId());
      TimerTask task = new TimerTask() {
        public void run() {
          System.out.println("Testting !!");
        }
      };

      Calendar date = Calendar.getInstance();
      date.set(Calendar.YEAR, estTime.getYear());
      date.set(Calendar.MONTH, estTime.getMonthValue());
      date.set(Calendar.DAY_OF_MONTH, estTime.getDayOfMonth());
      date.set(Calendar.HOUR_OF_DAY, estTime.getHour());
      date.set(Calendar.MINUTE, estTime.getMinute());
      date.set(Calendar.SECOND, estTime.getSecond());

      CountdownWaitingList countdownWaitingList = new CountdownWaitingList();
      countdownWaitingList.schedule(countdownWaitingList.getCountdownAction(), date.getTime());
//      timer.schedule(task, date.getTime());
//      countdownWaitingList.setTimer(timer);

      countdownWaitingLists.add(countdownWaitingList);
      System.out.println(countdownWaitingLists);

    });

  }
}
