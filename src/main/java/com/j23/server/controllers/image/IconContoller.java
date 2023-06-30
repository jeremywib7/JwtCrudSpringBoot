package com.j23.server.controllers.image;

import com.j23.server.services.icon.IconService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/icon")
@RequiredArgsConstructor
public class IconContoller {

    private final IconService iconService;

    @GetMapping("/download/main")
    public ResponseEntity<Resource> downloadMainIcon() throws IOException, URISyntaxException {
        return iconService.downloadMainIcon();
    }
}
