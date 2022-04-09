package com.j23.server.services.image;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.apache.commons.io.filefilter.DirectoryFileFilter.DIRECTORY;

@Service
public class ImageService {

    public static String home = System.getProperty("user.home");
    public static String productFolder = home + "/Desktop/Jeremy/Selfservice/Product/";
    public static String userFolder = home + "/Desktop/Jeremy/Selfservice/User/";

    public void uploadProductImage(String name, List<MultipartFile> files) throws IOException {
        if (files.isEmpty()) {
            throw new RuntimeException("File given is  not valid");
        }

        // create folder if not exists
        // folder format (productFolder + productName)
        Path pathFolder = Paths.get(productFolder + name);
        Files.createDirectories(pathFolder);

        // add in folder
        for (int i = 0; i < files.size(); i++) {
            String fileName = files.get(i).getOriginalFilename();

            // format (Folder/productName/image_0.jpeg)
            assert fileName != null;
            Path pathFile = Paths.get(pathFolder + "/" + i + "." + fileName.substring(fileName.lastIndexOf(".") + 1));
            try {
                Files.write(pathFile, files.get(i).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadProductImage(String imageName, String productName, HttpServletResponse response) {

        File fileToDownload = new File(productFolder + productName + "/" + imageName);

        if (!fileToDownload.exists()) {
            fileToDownload = new File(productFolder + "defaultproduct.png");
        }


        try (InputStream inputStream = new FileInputStream(fileToDownload)) {
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment: filename=" + imageName);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<Resource> downloadProductImageAsFile(String imageName, String productName) throws IOException {
        Path filePath = Paths.get(productFolder + productName).toAbsolutePath().normalize().resolve(imageName);

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(imageName + " was not found in the server");
        }

        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", imageName);
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
    }

    // delete a folder with product name
    public void deleteProductImage(String imageFolder) {
        try {
            Path pathFolder = Paths.get(productFolder + imageFolder);
            System.out.println("THE PATH : " + pathFolder);
            if (Files.exists(pathFolder)) {
                FileUtils.deleteDirectory(new File(String.valueOf(pathFolder)));
//                Files.delete(pathFolder);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
