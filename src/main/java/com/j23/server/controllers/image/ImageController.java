package com.j23.server.controllers.image;

import com.google.api.services.storage.Storage;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.StorageOptions;
import com.j23.server.services.image.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/images")
@Slf4j
public class ImageController {

  //    For Windows
//    String productFolder = "D:\\ImageData\\Product\\";
//    String userFolder = "D:\\ImageData\\User\\";

  //    //    For Mac
  String home = System.getProperty("user.home");
  String userFolder = home + "/Desktop/Jeremy/Selfservice/User/";

  @Autowired
  private ImageService imageService;

//  @PostMapping("/product/upload")
//  public ResponseEntity<?> uploadProductImage(
//    @RequestParam String productId,
//    @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {
//    if (files != null) {
//      imageService.uploadProductImage(productId, files);
//    }
//    return new ResponseEntity<>(HttpStatus.OK);
//  }

  @PostMapping("/product/upload")
  public Object upload(
    @RequestParam String productId,
    @RequestParam(value = "files", required = false) List<MultipartFile> multipartFileList) {
//    log.info("HIT -/upload | File Name : {}", multipartFile.getOriginalFilename());
    return imageService.uploadProductImage(multipartFileList);
  }

//  @PostMapping("/profile/pic/{fileName}")
//  public Object download(@PathVariable String fileName) throws IOException {
//    logger.info("HIT -/download | File Name : {}", fileName);
//    return fileService.download(fileName);
//  }

//  @GetMapping("/send-data")
//  public String testUploadGcp(File file) throws IOException {
//    BlobId blobId = BlobId.of("self-service-4820d.appspot.com", "demofile.txt");
//    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
//    Credentials credentials = GoogleCredentials.fromStream(Files.newInputStream(Paths.get("downloaded private key JSON file path")));
//    com.google.cloud.storage.Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
//    storage.create(blobInfo, Files.readAllBytes(file.toPath()));
//
//  }

  @GetMapping("/product/download")
  public void downloadProductImage(
    @RequestParam("imageName") String imageName,
    @RequestParam("productId") String productId,
    HttpServletResponse response) {
    imageService.downloadProductImage(imageName, productId, response);
  }

  @GetMapping("/product/download/file")
  public ResponseEntity<Resource> downloadProductImageAsFile(
    @RequestParam("imageName") String imageName,
    @RequestParam("productId") String productId) throws IOException {
    return imageService.downloadProductImageAsFile(imageName, productId);
  }

  // set required jwt to false
  // because this for customer
  @GetMapping("/customer/product/download/file")
  public ResponseEntity<Resource> downloadProductImageForCustomer(
    @RequestParam("imageName") String imageName,
    @RequestParam("productId") String productId) throws IOException {
    return imageService.downloadProductImageAsFile(imageName, productId);
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

  @PostMapping("/user/upload")
  public ResponseEntity<?> uploadUserImage(
    @RequestParam("name") String name,
    @RequestParam("file") MultipartFile file
  ) {

    if (file.isEmpty()) {
      throw new RuntimeException("File given is  not valid");
    }

    try {
      Path pathFolder = Paths.get(userFolder);
      Files.createDirectories(pathFolder);
      Path pathFile = Paths.get(userFolder + name);
      Files.write(pathFile, file.getBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/product/delete/")
  public ResponseEntity<Object> deleteProduct(@RequestParam String id) {
    imageService.deletePath(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
