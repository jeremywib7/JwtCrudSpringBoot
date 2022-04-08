package com.j23.server.services.image;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageService {

    String home = System.getProperty("user.home");
    String productFolder = home + "/Desktop/Jeremy/Selfservice/Product/";
    String userFolder = home + "/Desktop/Jeremy/Selfservice/User/";

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

    // delete a folder with product name
    public void deleteProductImage(String imageFolder) {
        try {
            Path pathFolder = Paths.get(productFolder + imageFolder);
            if (Files.exists(pathFolder)) {
                Files.delete(pathFolder);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
