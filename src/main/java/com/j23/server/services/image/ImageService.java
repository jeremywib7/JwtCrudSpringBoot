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

@Service
public class ImageService {

    public static String home = System.getProperty("user.home");
    public static String productFolder = home + "/Desktop/Jeremy/Selfservice/Product/";
    public static String userFolder = home + "/Desktop/Jeremy/Selfservice/User/";

    public void uploadProductImage(String productId, List<MultipartFile> files) throws IOException {
        if (!files.isEmpty()) {
            // folder format (productFolder + productName)
            Path pathFolder = Paths.get(productFolder + productId);

            // clean all folder in directory
            if (Files.exists(pathFolder)) {
                FileUtils.cleanDirectory(new File(String.valueOf(pathFolder)));
            }

            // create folder if not exists
            Files.createDirectories(pathFolder);

            // add in folder
            for (int i = 0; i < files.size(); i++) {
                String fileName = files.get(i).getOriginalFilename();

                // format (Folder/productName/index.jpeg)
                assert fileName != null;
                Path pathFile = Paths.get(pathFolder + "/" + i + "." + fileName.substring(fileName.lastIndexOf(".") + 1));
                try {
                    Files.write(pathFile, files.get(i).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void downloadProductImage(String imageName, String productId, HttpServletResponse response) {

        File fileToDownload = new File(productFolder + productId + "/" + imageName);

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

    public ResponseEntity<Resource> downloadProductImageAsFile(String imageName, String folderId) throws IOException {
        Path filePath = Paths.get(productFolder + folderId).toAbsolutePath().normalize().resolve(imageName);

        if (!Files.exists(filePath)) {
            filePath = Paths.get(productFolder + "defaultproduct.png");
        }

        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", imageName);
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
    }

    // delete a folder with product name
    public void deletePath(String folderId) {
        try {
            Path pathFolder = Paths.get(productFolder + folderId);
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
