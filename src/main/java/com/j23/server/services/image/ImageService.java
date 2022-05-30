package com.j23.server.services.image;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.j23.server.configuration.FirebaseConfig.*;

@Service
@Slf4j
public class ImageService {

  public static String home = System.getProperty("user.home");
  public static String productFolder = home + "/Desktop/Jeremy/Selfservice/Product/";
  public static String userFolder = home + "/Desktop/Jeremy/Selfservice/User/";


//  public void uploadProductImage(String productId, List<MultipartFile> files) throws IOException {
//    if (!files.isEmpty()) {
//      // folder format (productFolder + productName)
//      Path pathFolder = Paths.get(productFolder + productId);
//
//      // clean all folder in directory
//      if (Files.exists(pathFolder)) {
//        FileUtils.cleanDirectory(new File(String.valueOf(pathFolder)));
//      }
//
//      // create folder if not exists
//      Files.createDirectories(pathFolder);
//
//      // add in folder
//      for (int i = 0; i < files.size(); i++) {
//        String fileName = files.get(i).getOriginalFilename();
//
//        // format (Folder/productName/index.jpeg)
//        assert fileName != null;
//        Path pathFile = Paths.get(pathFolder + "/" + productId + "_" + i + "." + fileName.substring(fileName.lastIndexOf(".") + 1));
//        try {
//          Files.write(pathFile, files.get(i).getBytes());
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//      }
//    }
//
//  }

  // firebase cloud storage upload
  public void upload(String folderId, String folderName, List<MultipartFile> multipartFileList) {

    for (int i = 0; i < multipartFileList.size(); i++) {
      String fullFileName = multipartFileList.get(i).getOriginalFilename();

      try {
        assert fullFileName != null; // check if not null
        String formattedFileName = folderId + "_" + i + getExtension(fullFileName); // example : adawdadcfefadaxd_0.jpg
        String pathOfImage = folderName + folderId + "/" + formattedFileName;

        // bucket and blob (folder)
        BlobId blobId = BlobId.of(BUCKET, pathOfImage);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

        File file = this.convertToFile(multipartFileList.get(i), formattedFileName);  // to convert multipartFile to File
        GOOGLE_CLOUD_STORAGE.create(blobInfo, Files.readAllBytes(file.toPath()));     // upload to firebase storage
        file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public ResponseEntity<Object> download(String fileName, String productId, String folderName, HttpServletResponse response) throws IOException {
    String pathOfFile = folderName + productId + "/" + fileName;

    Blob blob = this.getImageBlobInfo(pathOfFile, folderName);

    ReadChannel reader = blob.reader();
    InputStream inputStream = Channels.newInputStream(reader);

    byte[] content = IOUtils.toByteArray(inputStream);
    final ByteArrayResource byteArrayResource = new ByteArrayResource(content);

    return ResponseEntity
      .ok()
      .contentLength(content.length)
      .header("Content-type", "application/octet-stream")
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
      .body(byteArrayResource);

  }

  private Blob getImageBlobInfo(String pathOfImage, String folderName) {
    Blob blob = GOOGLE_CLOUD_STORAGE.get(BlobId.of(BUCKET, pathOfImage));

    // if file doesn't exist in firebase storage
    if (blob == null) {
      // set path default product.png
      blob = GOOGLE_CLOUD_STORAGE.get(BlobId.of(BUCKET, folderName + "defaultproduct.png"));
    }
    return blob;
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

  public ResponseEntity<Resource> downloadAsFile(String imageName, String itemId, String folderName) throws IOException {
    String pathOfImage = folderName + itemId + "/" + imageName;

    Blob blob = GOOGLE_CLOUD_STORAGE.get(BlobId.of(BUCKET, pathOfImage));

    // if image doesn't exist in firebase storage
    if (blob == null) {
      // set path default product.png
      blob = GOOGLE_CLOUD_STORAGE.get(BlobId.of(BUCKET, folderName + "defaultproduct.png"));
    }

    ReadChannel reader = blob.reader();
    InputStream inputStream = Channels.newInputStream(reader);

    byte[] content = IOUtils.toByteArray(inputStream);

    File f = new File("someimg.jpg");

    Path path = Paths.get(f.getAbsolutePath());
    try {
      Files.write(path, content);
    } catch (IOException ignored) {

    }

    Resource resource = new UrlResource(path.toUri());
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("File-Name", imageName);
    httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());

    return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(path)))
      .headers(httpHeaders).body(resource);//        InputStreamResource resource = new InputStreamResource(new FileInputStream(content));
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Disposition", String.format("attachment; filename=your_file_name"));
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(content.length)
//                .contentType(MediaType.valueOf("application/octet-stream"))
//                .body(resource);

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
