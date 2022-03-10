package com.j23.server.services;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

@Service
public class ReportService {

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() {

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JasperPrint generateJasperPrint(String reportPath, Map<String, Object> map) throws IOException, JRException {
        InputStream targetStream = new ClassPathResource(reportPath).getInputStream();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(targetStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, getConnection());
//        targetStream.reset();
        return jasperPrint;

    }
}