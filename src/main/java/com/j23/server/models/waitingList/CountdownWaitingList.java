package com.j23.server.models.waitingList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Component
@Setter
@Getter
@ToString
public class CountdownWaitingList extends Timer {

  private String id =  String.valueOf(UUID.randomUUID());

  private String customerId;

  private CountdownAction countdownAction;

}
