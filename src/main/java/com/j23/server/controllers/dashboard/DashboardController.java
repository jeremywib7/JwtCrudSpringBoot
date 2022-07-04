package com.j23.server.controllers.dashboard;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.product.Product;
import com.j23.server.services.customer.customerOrder.CustomerOrderService;
import com.j23.server.services.dashboard.DashboardService;
import com.j23.server.services.dashboard.TotalSalesProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

  @Autowired
  private DashboardService dashboardService;

  @Autowired
  private TotalSalesProductService totalSalesProductService;

  @GetMapping()
  public ResponseEntity<Object> loadDashboardData() {
    return ResponseHandler.generateResponse("Successfully load dashboard data!", HttpStatus.OK, dashboardService
      .loadDashboardData());
  }

}
