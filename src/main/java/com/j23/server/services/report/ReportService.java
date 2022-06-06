package com.j23.server.services.report;

import com.j23.server.models.customer.customerOrder.CustomerOrder;
import com.j23.server.repos.customer.customerOrder.CustomerOrderRepository;
import com.j23.server.services.auth.UserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.j23.server.util.AppsConfig.*;

@Service
@Slf4j
public class ReportService {

  @Autowired
  UserService userService;

  @Autowired
  CustomerOrderRepository customerOrderRepository;

  DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
  DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  public ResponseEntity<byte[]> generateUserReport() throws Exception {
    JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(userService.findAllUser());
    return generatePdf(USER_REPORT_TITLE, beanCollectionDataSource, USER_REPORT_PATH);
  }

  public ResponseEntity<byte[]> generateSaleReport(LocalDateTime dateFrom, LocalDateTime dateTill) throws Exception {
    // if null set 1 month range

    HashMap<String, LocalDateTime> dateMap = checkDateRangeNotNull(dateFrom, dateTill);
    System.out.println("The from : " + dateMap.get("dateFrom"));
    System.out.println("The till : " + dateMap.get("dateTill"));

    List<CustomerOrder> customerOrderList = customerOrderRepository
      .findAllByOrderFinishedIsNotNullAndOrderFinishedBetweenOrderByOrderFinishedDesc(dateMap.get("dateFrom"), dateMap.get("dateTill"));

    if (customerOrderList.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.OK, "No data to export", null);
    }

    JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(customerOrderList);


    return generatePdf(SALES_REPORT_TITLE, beanCollectionDataSource, SALES_REPORT_PATH);
  }

  private ResponseEntity<byte[]> generatePdf(String title, JRBeanCollectionDataSource jrBeanCollectionDataSource,
                                             String path) throws Exception {

//    Path filePath = Paths.get(Objects.requireNonNull(getClass().getResource(path)).toURI());

    JasperReport jasperReport = JasperCompileManager.compileReport(new ClassPathResource(
      path).getInputStream());

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

  public HashMap<String, LocalDateTime> checkDateRangeNotNull(LocalDateTime dateFrom, LocalDateTime dateTill) {

    if (dateFrom == null) {
      dateFrom = LocalDateTime.parse(YearMonth.now().atDay(1).format(dateFormat) + " 00:00:00", df);
    }

    if (dateTill == null) {
      dateTill = LocalDateTime.parse(YearMonth.now().atEndOfMonth().format(dateFormat) + " 23:59:59", df);
    }

    HashMap<String, LocalDateTime> map = new HashMap<>();
    map.put("dateFrom", dateFrom);
    map.put("dateTill", dateTill);

    return map;

  }

  // list of successful customer order
  public List<CustomerOrder> loadAllSaleReportData() {
    return customerOrderRepository.findAllByOrderFinishedIsNotNull();
  }

  // list of successful customer order from date range
  // default 1 month
  public List<CustomerOrder> loadDateRangeSaleReportData(LocalDateTime dateFrom, LocalDateTime dateTill) {

    // if null set 1 month range
    HashMap<String, LocalDateTime> checkMap = checkDateRangeNotNull(dateFrom, dateTill);
    System.out.println("The from : " + checkMap.get("dateFrom"));
    System.out.println("The till : " + checkMap.get("dateTill"));

    return customerOrderRepository.findAllByOrderFinishedIsNotNullAndOrderFinishedBetweenOrderByOrderFinishedDesc(
      checkMap.get("dateFrom"), checkMap.get("dateTill")
    );
  }
}
