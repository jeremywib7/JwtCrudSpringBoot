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
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Setter
@Getter
@ToString
public class CustomerOrder {

  @Id
  private String id = String.valueOf(UUID.randomUUID());

  private int number = -1;

  private String status = "Waiting for payment";

  private boolean orderIsActive = false;

  @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "date_created", updatable = false)
  @CreationTimestamp
  @JsonView(Views.OrderDateOnlyViews.class)
  private LocalDateTime dateCreated;

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

  @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "date_updated")
  @UpdateTimestamp
  private LocalDateTime updatedOn;

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
