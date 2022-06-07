package com.j23.server.services.icon;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class IconService {

  public ResponseEntity<Resource> downloadMainIcon() throws IOException, URISyntaxException {

//    InputStream in = getClass().getResourceAsStream("/icons/hyper.ico");
//
//    Path filePath = Paths.get(getClass().getResourceAsStream("/icons/hyper.ico"));

    URL url = getClass().getResource("/icons/hyper.ico");

    assert url != null;
    Resource resource = new UrlResource(url);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("File-Name", "mainicon");
    httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());

    return ResponseEntity.ok().contentType(MediaType.parseMediaType(url.getFile()))
      .headers(httpHeaders).body(resource);
  }
}

