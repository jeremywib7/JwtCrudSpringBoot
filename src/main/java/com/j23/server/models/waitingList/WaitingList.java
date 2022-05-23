package com.j23.server.models.waitingList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitingList {

  private String id;
  private String username;
  private Long estTime;
  private int number = 0;

  @JsonProperty("isDone")
  private boolean isDone = false;

  private String status = "PROCESSING";
  private int steps = 2;
  private String messagingToken;

}
