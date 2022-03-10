package com.j23.server.controllers;

import com.j23.server.services.ReportService;
import com.j23.server.services.auth.UserService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    // USER
    @GetMapping("/user/list")
    public void userListReportPdf() throws JRException, IOException {
        httpServletResponse.reset();

        //        //Adding attribute names
        //        Map params = new HashMap<>();
        //        params.put("stid","stid");
        //        params.put("name","name");
        //        params.put("programme","programme");

//         chnage to pdf for stream
        httpServletResponse.setContentType("application/x-download");

        String localDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//         change attachement to inline for stream pdf
        httpServletResponse.setHeader("Content-Disposition", "attachment;  filename=" + "User List_" +
                localDateTime + ".pdf");

        JasperPrint jasperPrint = reportService.generateJasperPrint("reports/User_List.jasper", null);

        JasperExportManager.exportReportToPdfStream(jasperPrint, httpServletResponse.getOutputStream());


    }

}