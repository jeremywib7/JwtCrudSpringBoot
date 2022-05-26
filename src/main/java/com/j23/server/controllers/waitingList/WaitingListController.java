package com.j23.server.controllers.waitingList;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.note.Note;
import com.j23.server.models.waitingList.WaitingList;
import com.j23.server.services.waitingList.WaitingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
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
            }

        });
    }

    @PutMapping("/update/status")
    public ResponseEntity<Object> updateWaitingListStatus(@RequestBody WaitingList waitingList) {
        waitingListService.updateWaitingListStatus(waitingList, waitingList.getStatus(), waitingList.getSteps());

        return ResponseHandler.generateResponse("Successfully update customer waiting list status !",
                HttpStatus.OK, null);
    }

    @PutMapping("/update/status/ready-to-pickup")
    public ResponseEntity<Object> updateWaitingListStatusReadyToPickup(@RequestBody WaitingList waitingList) {
        waitingListService.updateStatusToReadyToPickup(waitingList);

        return ResponseHandler.generateResponse("Successfully update customer waiting list status !",
                HttpStatus.OK, null);
    }

    @PostMapping("/send-notification")
    public ResponseEntity<Object> sendNotification(@RequestBody Note note) {
        return ResponseHandler.generateResponse("Successfully send notification !", HttpStatus.OK,
                waitingListService.sendOrderDoneNotification(note));
    }
}
