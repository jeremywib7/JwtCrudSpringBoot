package com.j23.server.controllers.restaurant.report;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.services.restaurant.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


    @Autowired
    ReportService reportService;

    @GetMapping("/user")
    public ResponseEntity<byte[]> generateUserReportPdf() throws Exception {
        return reportService.generateUserReport();
    }

    @GetMapping("/sale-report")
    public ResponseEntity<byte[]> generateSaleReportPdf(
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime dateTill
    ) throws Exception {
        return reportService.generateSaleReport(dateFrom, dateTill);
    }

    @GetMapping("/sale-report/data")
    public ResponseEntity<Object> loadDateRangeSaleReportData(
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime dateTill
    ) {
        List<CustomerOrder> customerOrderList = reportService.loadDateRangeSaleReportData(dateFrom, dateTill);
        return ResponseHandler.generateResponse("Successfully load all sales report data!",
                HttpStatus.OK, customerOrderList);
    }

}
