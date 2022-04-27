package com.j23.server.models.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.j23.server.models.product.Product;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Data
@Setter
@Getter
@ToString
public class OrderedProduct {

  @Id
  private String id;

  @JsonIncludeProperties(value = {"id", "name", "unitPrice", "discount", "discountedPrice"})
  @OneToOne(targetEntity = Product.class)
  private Product product;

  @Min(0)
  @Max(100000)
  @Column(name = "quantity")
  private Integer quantity;

  @JsonFormat(pattern="MM/dd/yyyy HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "date_created")
  private LocalDateTime createdOn;

  @JsonFormat(pattern="MM/dd/yyyy HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "date_updated")
  private LocalDateTime updatedOn;
}
