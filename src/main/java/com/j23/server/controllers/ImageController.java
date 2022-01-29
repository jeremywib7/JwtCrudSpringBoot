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
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RequestMapping("/images")
public class ImageController {

    @GetMapping("/product/download/{name}")
    public void downloadProductImage(
            @PathVariable("name") String name,
            HttpServletResponse response) {

        File fileToDownload = new File("D:\\ImageData\\Product\\" + name);

        if (!fileToDownload.exists()) {
            fileToDownload = new File("D:\\ImageData\\Product\\defaultproduct.jpg");
        }

        try (InputStream inputStream = new FileInputStream(fileToDownload)) {
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment: filename=" + name);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/user/download/{username}")
    public void downloadUserImage(
            @PathVariable("username") String username,
            HttpServletResponse response) {
        File fileToDownload = new File("D:\\ImageData\\User\\" + username);

        if (!fileToDownload.exists()) {
            fileToDownload = new File("D:\\ImageData\\User\\defaultuser.png");
        }

        try (InputStream inputStream = new FileInputStream(fileToDownload)) {
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment: filename=" + username);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/product/upload")
    public ResponseEntity<?> uploadProductImage(
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file
    ) {
        String folder = "D:\\ImageData\\Product\\";

        if (file.isEmpty()) {
            throw new RuntimeException("File given is  not valid");
        }

        String fileName = file.getOriginalFilename();

        try {
            Path pathFolder = Paths.get(folder);
            Files.createDirectories(pathFolder);
//            Path pathFile = Paths.get(folder + username +"." + fileName.substring(fileName.lastIndexOf(".") + 1));

            Path pathFile = Paths.get(folder + name + "." + fileName.substring(fileName.lastIndexOf(".") + 1));

            Files.write(pathFile, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/user/upload")
    public ResponseEntity<?> uploadUserImage(
            @RequestParam("username") String username,
            @RequestParam("file") MultipartFile file
    ) {
        String folder = "D:\\ImageData\\User\\";

        if (file.isEmpty()) {
            throw new RuntimeException("File given is  not valid");
        }

        String fileName = file.getOriginalFilename();

        try {
            Path pathFolder = Paths.get(folder);
            Files.createDirectories(pathFolder);
            Path pathFile = Paths.get(folder + username + "." + fileName.substring(fileName.lastIndexOf(".") + 1));

            Files.write(pathFile, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/user/delete/{imageUrl}")
    public void deleteFile(@PathVariable("imageUrl") String imageUrl) {

        String folder = "D:\\ImageData\\Product\\";

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
