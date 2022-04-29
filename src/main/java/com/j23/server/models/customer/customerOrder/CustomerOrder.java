package com.j23.server.models.customer.customerOrder;

import com.j23.server.models.customer.CustomerProfile;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Setter
@Getter
@ToString
public class CustomerOrder {

  @Id
  private String id;

  private int number;

  @OneToOne
  @JoinColumn(name = "customer_profile")
  private CustomerProfile customerProfile;

  @OneToMany
  @JoinColumn(name = "customer_order_id")
  private List<OrderProductList> orderProductLists;

  private BigDecimal totalPrice;

}
