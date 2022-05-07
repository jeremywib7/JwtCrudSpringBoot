package com.j23.server.models.product;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Setter
@Getter
@ToString
@Table(name = "product")
@SQLDelete(sql = "UPDATE product SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Product implements Serializable {

  @Id
  @Column(nullable = false)
  private String id;

  @Column(length = 25)
  @JsonView(Views.ProductNameViews.class)
  private String name;

  private Long totalCalories;

  @Column(length = 100)
  private String description;

  private boolean discount;

  @Column(precision = 13, scale = 2, name = "unit_price")
  private BigDecimal unitPrice;

  @Column(precision = 13, scale = 2, name = "discounted_price")
  private BigDecimal discountedPrice;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "date_created")
  private LocalDateTime createdOn;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @Column(name = "last_updated")
  private LocalDateTime updatedOn;

  @ManyToOne()
  @JoinColumn(name = "category_id")
  private ProductCategory category;

  private boolean active;

//  @JsonIgnore
  private boolean deleted = Boolean.FALSE;

  @JsonManagedReference
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private List<ProductImage> images;


}
