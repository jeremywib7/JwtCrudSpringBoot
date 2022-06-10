package com.j23.server.models.dashboard;

import com.j23.server.models.customer.customerOrder.CustomerOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dashboard {

  private Long totalOrders;
  private BigDecimal totalRevenue;
  private Long totalCustomers;
  private Long totalProducts;
  private List<CustomerOrder> recentOrder;
  private List<TotalSalesProduct> top5BestSales;

}
