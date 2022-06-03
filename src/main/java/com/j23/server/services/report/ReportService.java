package com.j23.server.services.report;

import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.models.report.SaleReport;
import com.j23.server.repos.customer.customerOrder.CustomerOrderRepository;
import com.j23.server.services.auth.UserService;
import jdk.vm.ci.meta.Local;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.j23.server.util.AppsConfig.USER_REPORT_PATH;
import static com.j23.server.util.AppsConfig.USER_REPORT_TITLE;

@Service
public class ReportService {

  @Autowired
  UserService userService;

  @Autowired
  CustomerOrderRepository customerOrderRepository;

  public ResponseEntity<byte[]> generateUserReport() throws IOException, JRException {
    JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(userService.findAllUser());
    return generatePdf(USER_REPORT_TITLE, beanCollectionDataSource, USER_REPORT_PATH);
  }

  public ResponseEntity<byte[]> generateSaleReport(String dateFrom, String dateTill) throws IOException, JRException {

    LocalDateTime from = LocalDateTime.from(LocalDateTime.parse(dateFrom));
    LocalDateTime till = LocalDateTime.from(LocalDateTime.parse(dateTill));

    List<CustomerOrder> customerOrderList = customerOrderRepository
      .findAllByOrderFinishedIsNotNullAndOrderFinishedBetweenOrderByOrderFinishedDesc(from, till);

    List<SaleReport> saleReportList = new ArrayList<>();

    customerOrderList.stream().map(customerOrder -> {
      SaleReport saleReport = new SaleReport();
      saleReport.setOrderCreated(customerOrder.getDateCreated());
      saleReport.setOrderFinished(customerOrder.getOrderFinished());
      saleReport.setCustomerId(customerOrder.getId());
      saleReport.setTotal(customerOrder.getTotalPrice());

      return saleReportList.add(saleReport);
    }).collect(Collectors.toList()).clear();

    JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(saleReportList);

    return generatePdf(USER_REPORT_TITLE, beanCollectionDataSource, USER_REPORT_PATH);
  }

  private ResponseEntity<byte[]> generatePdf(String title, JRBeanCollectionDataSource jrBeanCollectionDataSource,
                                            String path) throws IOException, JRException {
    JasperReport jasperReport = JasperCompileManager.compileReport(Files.newInputStream(Paths.get(path)));

    HashMap<String, Object> map = new HashMap<>();
    map.put("title", title);

    JasperPrint report = JasperFillManager.fillReport(jasperReport, map, jrBeanCollectionDataSource);

    byte[] data = JasperExportManager.exportReportToPdf(report);

    String localDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    HttpHeaders headers = new HttpHeaders();

    //        set "inline" for see pdf in browser || set "attachment" for download pdf
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + title + "_" + localDateTime + ".pdf");

    return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
  }
}
