package com.j23.server.controllers;

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

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RequestMapping("/images")
public class ImageController {

    //    For Windows
    String productFolder = "D:\\ImageData\\Product\\";
    String userFolder = "D:\\ImageData\\User\\";

//    //    For Mac
//    String home = System.getProperty("user.home");
//    String productFolder = home + "/Desktop/Jeremy/Selfservice/Product/";
//    String userFolder = home + "/Desktop/Jeremy/Selfservice/User/";

    @GetMapping("/product/download/{name}")
    public void downloadProductImage(
            @PathVariable("name") String name,
            HttpServletResponse response) {

        File fileToDownload = new File(productFolder + name);


        if (!fileToDownload.exists()) {
            fileToDownload = new File(productFolder + "defaultproduct.png");
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
        File fileToDownload = new File(userFolder + username);

        if (!fileToDownload.exists()) {
            fileToDownload = new File(userFolder + "defaultuser.png");
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

        if (file.isEmpty()) {
            throw new RuntimeException("File given is  not valid");
        }

        String fileName = file.getOriginalFilename();

        try {
            Path pathFolder = Paths.get(productFolder);
            Files.createDirectories(pathFolder);
            Path pathFile = Paths.get(productFolder + name + "." + fileName.substring(fileName.lastIndexOf(".") + 1));

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

        if (file.isEmpty()) {
            throw new RuntimeException("File given is  not valid");
        }

        String fileName = file.getOriginalFilename();

        try {
            Path pathFolder = Paths.get(userFolder);
            Files.createDirectories(pathFolder);
            Path pathFile = Paths.get(userFolder + username + "." + fileName.substring(fileName.lastIndexOf(".") + 1));

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
