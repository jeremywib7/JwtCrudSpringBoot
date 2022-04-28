package com.j23.server.models.customer.customerOrder;

import com.j23.server.models.product.Product;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Setter
@Getter
@ToString
public class OrderProductList {

  @Id
  private String id;

  // to show if product changed
  // so product can be tracked
  @OneToOne
  @JoinColumn(name = "product_id")
  private Product product;
  //


  // product info

  private String name;

  private int quantity;

  private boolean discount;

  @Column(precision = 13, scale = 2, name = "unit_price")
  private BigDecimal unitPrice;

  @Column(precision = 13, scale = 2, name = "discounted_price")
  private BigDecimal discountedPrice;

  //
}
