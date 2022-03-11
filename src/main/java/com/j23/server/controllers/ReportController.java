package com.j23.server.controllers;

import com.j23.server.services.ReportService;
import com.j23.server.services.auth.UserService;
import com.j23.server.services.product.ProductService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @GetMapping("/user/list")
    public String generatePdf() throws FileNotFoundException, JRException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(userService.findAllUser());
        JasperReport jasperReport = JasperCompileManager.compileReport(new FileInputStream(
                "src/main/resources/reports/User.jrxml"));
        HashMap<String, Object> map = new HashMap<>();
        JasperPrint report = JasperFillManager.fillReport(jasperReport, null, beanCollectionDataSource);
        JasperExportManager.exportReportToPdfFile(report, "invoice.pdf");

        return beanCollectionDataSource.toString();
    }

}