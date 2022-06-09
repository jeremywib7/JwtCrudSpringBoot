package com.j23.server.models.waitingList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditTimerWaitingList {

  private int estHour;
  private int estMinute;
  private int estSecond;
  private String customerId;
}
