package com.j23.server.models.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.j23.server.Views;
import com.j23.server.models.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalSalesProduct {

  @Id
  private String id = String.valueOf(UUID.randomUUID());

  @OneToOne
  @JsonView(Views.BestSellerOnlyViews.class)
  private Product productDetail;

  @JsonFormat(pattern = "MM/dd/yyyy HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @CreationTimestamp()
  @Column(updatable = false)
  private LocalDateTime createdOn;

  private BigDecimal totalProfit;

  private BigDecimal percentageProfit;

}
