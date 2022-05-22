package com.j23.server.controllers.image;

import com.j23.server.services.icon.IconService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Objects;

@RestController
@RequestMapping("/icon")
public class IconContoller {

    @Autowired
    private IconService iconService;

    @GetMapping("/download/main")
    public ResponseEntity<Resource> downloadMainIcon() throws IOException, URISyntaxException {
        return iconService.downloadMainIcon();
    }
}
