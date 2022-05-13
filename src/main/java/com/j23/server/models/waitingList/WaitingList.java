package com.j23.server.models.waitingList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.math.BigDecimal;

@Component
@Setter
@Getter
@ToString
public class WaitingList {

  @Id
  private String id;

  private String username;

  private Long estTime;

  private int number = 0;

  private String status = "PROCESSING";

  private int steps = 2;

}
