package com.j23.server.controllers.report;

import com.j23.server.services.report.ReportService;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/reports")
public class ReportController {

  @Autowired
  ReportService reportService;

  @GetMapping("/user")
  public ResponseEntity<byte[]> generateUserReportPdf() throws IOException, JRException {
    return reportService.generateUserReport();
  }

  @GetMapping("/sale-report")
  public ResponseEntity<byte[]> generateSaleReportPdf(
    @RequestParam String dateFrom,
    @RequestParam String dateTill
  ) throws IOException, JRException {
    return reportService.generateSaleReport(dateFrom, dateTill);
  }

}
