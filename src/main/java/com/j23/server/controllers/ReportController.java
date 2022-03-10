package com.j23.server.controllers;

import com.j23.server.services.ReportService;

import javax.servlet.http.HttpServletResponse;

public class ReportController {

    private ReportService reportService;

    private HttpServletResponse httpServletResponse;

    public void userListReportPdf() {
        httpServletResponse.setContentType("application/pdf");
    }
}
