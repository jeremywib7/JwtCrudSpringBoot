package com.j23.server.controllers.waitingList;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.j23.server.models.waitingList.WaitingList;
import com.j23.server.services.waitingList.WaitingListService;
import org.springframework.beans.factory.annotation.Autowired;
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

    Firestore firestore = FirestoreClient.getFirestore();

    ApiFuture<QuerySnapshot> apiFuture = firestore.collection("Waiting_List").get();
    List<QueryDocumentSnapshot> list = apiFuture.get().getDocuments();
    List<WaitingList> waitingListList = list.stream().map((doc) -> doc.toObject(WaitingList.class)).collect(Collectors.toList());

    waitingListList.forEach(waitingList -> {

      LocalDateTime currentDateTime = LocalDateTime.now();
      LocalDateTime estimatedTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(waitingList.getEstTime()), TimeZone.getDefault().toZoneId());

      // check if estimated time is already passed or equal with current time
      if (currentDateTime.isBefore(estimatedTime) || currentDateTime.isEqual(estimatedTime)) {
        waitingListService.addToCountdownWaitingList(waitingList);
      } else {
        waitingListService.updateWaitingListStatus(waitingList, "Waiting", 2);
      }

    });
  }
}
