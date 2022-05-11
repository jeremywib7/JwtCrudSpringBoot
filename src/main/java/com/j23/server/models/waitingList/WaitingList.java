package com.j23.server.models.waitingList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.time.Instant;

@Component
@Setter
@Getter
@ToString
public class WaitingList {

  @Id
  private String id;

  private String customerName;

  private int estHour;

  private int estMinute;

  private int estSecond;

  private Long estTime;

  private int number;

  private String status;

}
