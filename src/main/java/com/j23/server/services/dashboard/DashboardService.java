package com.j23.server.services.dashboard;

import com.j23.server.models.dashboard.Dashboard;
import com.j23.server.services.customer.CustomerProfileService;
import com.j23.server.services.customer.customerOrder.CustomerOrderService;
import com.j23.server.services.product.ProductService;
import com.j23.server.services.time.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardService {

  @Autowired
  private CustomerOrderService customerOrderService;

  @Autowired
  private CustomerProfileService customerProfileService;

  @Autowired
  private ProductService productService;

  public Dashboard loadDashboardData() {

    Dashboard dashboard = new Dashboard();
    BigDecimal totalRevenue = customerOrderService.getTotalRevenue();

    dashboard.setTotalOrders(customerOrderService.getTotalOrdersForCurrentMonth());
    dashboard.setTotalRevenue(totalRevenue != null ? totalRevenue : new BigDecimal(0));
    dashboard.setTotalCustomers(customerProfileService.getTotalCustomers());
    dashboard.setTotalProducts(productService.getTotalProducts());

    return dashboard;

  }

}
