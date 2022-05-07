package com.j23.server.models.waitingList;

import com.j23.server.models.auth.User;
import com.j23.server.models.customer.CustomerProfile;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

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

  private int estTime;

  private int number;

  private String status;

}
