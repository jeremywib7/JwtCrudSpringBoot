package com.j23.server.services.image;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

  private static Object TEMP_URL = null;
  private static final String DOWNLOAD_URL = "";
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
        Path pathFile = Paths.get(pathFolder + "/" + productId + "_" + i + "." + fileName.substring(fileName.lastIndexOf(".") + 1));
        try {
          Files.write(pathFile, files.get(i).getBytes());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }

  // firebase cloud storage upload
  public Object uploadProductImage(List<MultipartFile> multipartFileList) {

    for (int i = 0; i < multipartFileList.size(); i++) {
      String fullFileName = multipartFileList.get(i).getOriginalFilename();

      try {
        assert fullFileName != null; // check if not null
        String formattedFileName = i + getExtension(fullFileName); // example : 0.jpg

        File file = this.convertToFile(multipartFileList.get(i), formattedFileName);                      // to convert multipartFile to File
        this.uploadFile(file, "Product/"+formattedFileName);                                   // to get uploaded file link
        file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return null;                     // Your customized response

  }


  private void uploadFile(File file, String filePath) throws IOException {
    // bucket and blob
    BlobId blobId = BlobId.of("self-service-4820d.appspot.com", filePath);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

    // get firebase json
    Resource resource = new ClassPathResource("config/serviceAccountKey.json");
    InputStream serviceAccount = resource.getInputStream();
    Credentials credentials = GoogleCredentials.fromStream(serviceAccount);

    // storage
    Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    storage.create(blobInfo, Files.readAllBytes(file.toPath()));
//    return String.format(DOWNLOAD_URL, URLEncoder.encode(filePath, StandardCharsets.UTF_8));
  }

  private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
    File tempFile = new File(fileName);
    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
      fos.write(multipartFile.getBytes());
      fos.close();
    }
    return tempFile;
  }

  private String getExtension(String fileName) {
    return fileName.substring(fileName.lastIndexOf("."));
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
