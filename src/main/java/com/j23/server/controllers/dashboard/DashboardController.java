package com.j23.server.controllers.dashboard;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.product.Product;
import com.j23.server.services.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

  @Autowired
  private DashboardService dashboardService;

  @GetMapping()
  public ResponseEntity<Object> loadDashboardData() {
    return ResponseHandler.generateResponse("Successfully load dashboard data!", HttpStatus.OK, dashboardService
      .loadDashboardData());
  }
}
