package com.j23.server.controllers.image;

import com.j23.server.configuration.ResponseHandler;
import com.j23.server.services.image.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Objects;

import static com.j23.server.util.AppsConfig.PRODUCT_FOLDER;
import static com.j23.server.util.AppsConfig.USER_FOLDER;

@RestController
@RequestMapping("/images")
@Slf4j
public class ImageController {

    @Autowired
    private ImageService imageService;

    //    For Windows
    //    String productFolder = "D:\\ImageData\\Product\\";
    //    String userFolder = "D:\\ImageData\\User\\";

    //    //    For Mac
    String home = System.getProperty("user.home");
    String userFolder = home + "/Desktop/Jeremy/Selfservice/User/";

    @PostMapping("/product/upload")
    public ResponseEntity<?> uploadProductImage(
            @RequestParam String productId,
            @RequestParam(value = "files", required = false) List<MultipartFile> multipartFileList) {
        imageService.uploadListOfFile(productId, PRODUCT_FOLDER, multipartFileList);

        return ResponseHandler.generateResponse("Successfully upload image!",
                HttpStatus.OK, null);
    }

    @GetMapping("/product/download")
    public ResponseEntity<Object> downloadProductImage(
            @RequestParam("imageName") String imageName,
            @RequestParam("productId") String productId) throws IOException {
        return imageService.download(imageName, productId, PRODUCT_FOLDER);
    }

    @GetMapping(path = "/product/download/file", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> downloadProductImageAsFile(
            @RequestParam("imageName") String imageName,
            @RequestParam("productId") String productId) throws IOException {
        return imageService.downloadAsFile(imageName, productId, PRODUCT_FOLDER);
    }

    @PostMapping("/user/upload")
    public ResponseEntity<?> uploadUserImage(
            @RequestParam("id") String id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        imageService.uploadFile(id, USER_FOLDER, file, "profile_picture." +
                imageService.getExtension(Objects.requireNonNull(file.getOriginalFilename())));

        return ResponseHandler.generateResponse("Successfully upload image!",
                HttpStatus.OK, null);
    }

    @GetMapping("/user/download")
    public ResponseEntity<Object> downloadUserImage(
            @RequestParam("userId") String userId,
            @RequestParam("imageName") String imageName // to get extension of image
    ) throws IOException {
        return imageService.download(imageName, userId, USER_FOLDER);
    }

    // set required jwt to false
    // because this for customer
    @GetMapping("/customer/product/download/file")
    public ResponseEntity<Resource> downloadProductImageForCustomer(
            @RequestParam("imageName") String imageName,
            @RequestParam("productId") String productId) throws IOException {
        return imageService.downloadAsFile(imageName, productId, PRODUCT_FOLDER);
    }

//  @GetMapping("/user/download/{username}")
//  public void downloadUserImage(
//    @PathVariable("username") String username,
//    HttpServletResponse response) {
//    File fileToDownload = new File(userFolder + username);
//
//    if (!fileToDownload.exists()) {
//      fileToDownload = new File(userFolder + "defaultuser.png");
//    }
//
//    try (InputStream inputStream = new FileInputStream(fileToDownload)) {
//      response.setContentType("application/force-download");
//      response.setHeader("Content-Disposition", "attachment: filename=" + username);
//      IOUtils.copy(inputStream, response.getOutputStream());
//      response.flushBuffer();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//  }

    @DeleteMapping("/product/delete/")
    public ResponseEntity<Object> deleteProduct(@RequestParam String productId) {
        imageService.resetAllFilesInDirectory(PRODUCT_FOLDER, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PostMapping("/user/upload")
//    public ResponseEntity<?> uploadUserImage(
//            @RequestParam("name") String name,
//            @RequestParam("file") MultipartFile file
//    ) {
//
//        if (file.isEmpty()) {
//            throw new RuntimeException("File given is  not valid");
//        }
//
//        try {
//            Path pathFolder = Paths.get(userFolder);
//            Files.createDirectories(pathFolder);
//            Path pathFile = Paths.get(userFolder + name);
//            Files.write(pathFile, file.getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

}
