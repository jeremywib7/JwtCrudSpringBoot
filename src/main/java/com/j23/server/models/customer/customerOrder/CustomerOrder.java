package com.j23.server.models.customer.customerOrder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.j23.server.Views;
import com.j23.server.models.customer.CustomerProfile;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrder {

  @Id
  private String id = String.valueOf(UUID.randomUUID());

  private int number = -1;

  private boolean orderIsActive = false;

  @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(updatable = false)
  @CreationTimestamp
  @JsonView(Views.OrderDateOnlyViews.class)
  private LocalDateTime dateTimeCreated;

  @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "order_processed")
  @JsonView(Views.OrderDateOnlyViews.class)
  private LocalDateTime orderProcessed;

  @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "order_finished")
  @JsonView(Views.OrderDateOnlyViews.class)
  private LocalDateTime orderFinished;

//  private Date orderDateFinished;
//
//  private Time orderTimeFinished;

  @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @UpdateTimestamp
  private LocalDateTime dateTimeUpdated;

  @OneToOne
  @JoinColumn(name = "customer_profile")
  private CustomerProfile customerProfile;

  @OneToMany
  @JoinColumn(name = "customer_order_id")
  private List<HistoryProductOrder> historyProductOrders;

  private BigDecimal totalPaid;

  private BigDecimal totalChange;

  private BigDecimal totalPrice;

  private int estHour = 0;

  private int estMinute = 0;

  private int estSecond = 0;

  @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime estTime;

}
