package com.j23.server.models.waitingList;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountdownWaitingList {

  private String customerId;

  private Timer estimatedTime = new Timer();

  private TimerTask onFinished;

}
