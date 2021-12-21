package com.j23.server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin
@RequestMapping("/images")
public class ImageController {

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("File given is  not valid");
        }

        String folder = "C:\\Users\\ADMIN14-SB1\\Pictures\\uploads\\";

        try {
            Path pathFolder = Paths.get(folder);
            Files.createDirectories(pathFolder);

            Path pathFile =  Paths.get(folder + file.getOriginalFilename());
            Files.write(pathFile, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
