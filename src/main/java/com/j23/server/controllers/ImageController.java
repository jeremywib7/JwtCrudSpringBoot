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
@RequestMapping("/images")
public class ImageController {

    @CrossOrigin
    @GetMapping("{username}")
    public void downloadUserImage(@PathVariable("username") String username, HttpServletResponse response) {
        try {
            File fileToDownload = new File("D:\\ImageData\\"+ username);

            try (InputStream inputStream  = new FileInputStream(fileToDownload)) {
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", "attachment: filename=" + username);
                IOUtils.copy(inputStream, response.getOutputStream());
                response.flushBuffer();
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity<?> uploadUserImage(
            @RequestParam("username") String username,
            @RequestParam("file") MultipartFile file
    ) {

        if (file.isEmpty()) {
            throw new RuntimeException("File given is  not valid");
        }

        String folder = "D:\\ImageData\\"; // my file path
        String fileName = file.getOriginalFilename();

        try {
            Path pathFolder = Paths.get(folder);
            Files.createDirectories(pathFolder);
//            Path pathFile = Paths.get(folder + username +"." + fileName.substring(fileName.lastIndexOf(".") + 1));

            Path pathFile = Paths.get(folder + username+"." + fileName.substring(fileName.lastIndexOf(".") + 1));

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
