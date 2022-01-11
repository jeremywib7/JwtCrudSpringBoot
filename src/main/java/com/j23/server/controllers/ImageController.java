package com.j23.server.controllers;

import com.j23.server.models.auth.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin
@RequestMapping("/images")
public class ImageController {

    private User user;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("File given is  not valid");
        }

        String folder = "D:\\ImageData\\"; // my file path

        try {
            Path pathFolder = Paths.get(folder);
            Files.createDirectories(pathFolder);
            Path pathFile = Paths.get(folder + file.getOriginalFilename());

            Files.write(pathFile, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

//    public static String getResourceFileAsString(String folder) {
//        InputStream is = getResourceFileAsInputStream(folder);
//        if (is != null) {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//            return (String)reader.lines().collect(Collectors.joining(System.lineSeparator()));
//        } else {
//            throw new RuntimeException("resource not found");
//        }
//    }
//
//    public static InputStream getResourceFileAsInputStream(String folder) {
//        ClassLoader classLoader = ImageController.class.getClassLoader();
//        return classLoader.getResourceAsStream(folder);
//    }
}
