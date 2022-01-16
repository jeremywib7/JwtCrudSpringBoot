package com.j23.server.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j23.server.configuration.ResponseHandler;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;

@RestController
@CrossOrigin
@RequestMapping("/images")
public class ImageController {

    JsonNode json;
    ObjectMapper mapper = new ObjectMapper();

    String folder = "D:\\ImageData\\"; // my file path

    @GetMapping("/{username}")
    public void downloadUserImage(
            @PathVariable("username") String username,
            @RequestParam("accessToken") String accessToken,
            HttpServletResponse response) {

        try {
            File fileToDownload = new File("D:\\ImageData\\" + username);

            try (InputStream inputStream = new FileInputStream(fileToDownload)) {
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", "attachment: filename=" + username);
                IOUtils.copy(inputStream, response.getOutputStream());
                response.flushBuffer();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> uploadUserImage(
            @RequestParam("username") String username,
            @RequestParam("file") MultipartFile file
    ) {

        if (file.isEmpty()) {
            throw new RuntimeException("File given is  not valid");
        }

        String fileName = file.getOriginalFilename();

        try {
            Path pathFolder = Paths.get(folder);
            Files.createDirectories(pathFolder);
//            Path pathFile = Paths.get(folder + username +"." + fileName.substring(fileName.lastIndexOf(".") + 1));

            Path pathFile = Paths.get(folder + username + "." + fileName.substring(fileName.lastIndexOf(".") + 1));

            Files.write(pathFile, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{imageUrl}")
    public void deleteFile(@PathVariable("imageUrl") String imageUrl) {

        try {
            Path pathFile = Paths.get(folder + imageUrl);
            if (Files.exists(pathFile)) {
                Files.delete(pathFile);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
