package com.j23.server.services.report;

import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.repos.customer.customerOrder.CustomerOrderRepository;
import com.j23.server.services.auth.UserService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import static com.j23.server.util.AppsConfig.*;

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

    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    LocalDateTime from = LocalDateTime.parse(dateFrom + " 00:00:00", df);
    LocalDateTime till = LocalDateTime.parse(dateTill + " 23:59:59", df);

    List<CustomerOrder> customerOrderList = customerOrderRepository
      .findAllByOrderFinishedIsNotNullAndOrderFinishedBetweenOrderByOrderFinishedDesc(from, till);

    if (customerOrderList.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.OK, "No data to export", null);
    }

    JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(customerOrderList);

    return generatePdf(SALES_REPORT_TITLE, beanCollectionDataSource, SALES_REPORT_PATH);
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
