package com.j23.server.controllers.image;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/icon")
public class IconContoller {

  @GetMapping("/download/main")
  public void downloadMainIcon(HttpServletResponse response) {

    File fileToDownload = new File("/icons/hyper.svg");

    try (InputStream inputStream = new FileInputStream(fileToDownload)) {
      response.setContentType("application/force-download");
      response.setHeader("Content-Disposition", "attachment: filename=main");
      IOUtils.copy(inputStream, response.getOutputStream());
      response.flushBuffer();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
