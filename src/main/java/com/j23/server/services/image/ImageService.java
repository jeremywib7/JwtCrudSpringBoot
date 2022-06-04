package com.j23.server.services.image;

import com.google.api.gax.paging.Page;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import static com.j23.server.configuration.FirebaseConfig.*;

@Service
@Slf4j
public class ImageService {

    public static String home = System.getProperty("user.home");
    public static String productFolder = home + "/Desktop/Jeremy/Selfservice/Product/";
    public static String userFolder = home + "/Desktop/Jeremy/Selfservice/User/";


    // firebase cloud storage upload
    public void uploadListOfFile(String folderId, String folderName, List<MultipartFile> multipartFileList) {

        // reset file for current id
        resetAllFilesInDirectory(folderName, folderId);

        for (int i = 0; i < multipartFileList.size(); i++) {
            String fullFileName = multipartFileList.get(i).getOriginalFilename();

            try {
                assert fullFileName != null; // check if not null
                String formattedFileName = folderId + "_" + i + "." + getExtension(fullFileName); // example : someid_0.jpg
                String pathOfFile = folderName + folderId + "/" + formattedFileName;

                // bucket and blob (folder)
                BlobId blobId = BlobId.of(BUCKET, pathOfFile);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/" + getExtension(fullFileName)).build();

                File file = this.convertToFile(multipartFileList.get(i), formattedFileName);  // to convert multipartFile to File
                GOOGLE_CLOUD_STORAGE.create(blobInfo, Files.readAllBytes(file.toPath()));     // upload to firebase storage
                file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ResponseEntity<Object> download(String fileName, String folderId, String folderName) throws IOException {
        String pathOfFile = folderName + folderId + "/" + fileName;

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

    public void uploadFile(String folderId, String folderName, MultipartFile multipartFile,
                           String formattedFileName) throws IOException {

        // reset file for current id
        resetAllFilesInDirectory(folderName, folderId);

        String fullFileName = multipartFile.getOriginalFilename();
        assert fullFileName != null;
        String pathOfFile = folderName + folderId + "/" + formattedFileName;

        BlobId blobId = BlobId.of(BUCKET, pathOfFile);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/" + getExtension(fullFileName)).build();

        File file = this.convertToFile(multipartFile, formattedFileName);  // to convert multipartFile to File
        GOOGLE_CLOUD_STORAGE.create(blobInfo, Files.readAllBytes(file.toPath()));     // upload to firebase storage

    }

    public void resetAllFilesInDirectory(String folderName, String folderId) {
        Page<Blob> blobs = GOOGLE_CLOUD_STORAGE.list(BUCKET, Storage.BlobListOption.currentDirectory(),
                Storage.BlobListOption.prefix(folderName + folderId + "/"));
        Iterator<Blob> blobIterator = blobs.iterateAll().iterator();
        while (blobIterator.hasNext()) {
            Blob blob = blobIterator.next();
            BUCKET_STORAGE_CLIENT.get(blob.getName()).delete();
            log.info("the blob is : " + blob);
        }
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
        }
        return tempFile;
    }

    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public ResponseEntity<Resource> downloadAsFile(String fileName, String itemId, String folderName) throws IOException {
        String pathOfFile = folderName + itemId + "/" + fileName;

        Blob blob = this.getImageBlobInfo(pathOfFile, folderName);

        // if image doesn't exist in firebase storage
        if (blob == null) {
            // set path default product.png
            blob = GOOGLE_CLOUD_STORAGE.get(BlobId.of(BUCKET, folderName + "defaultproduct.png"));
        }

        System.out.println("The blob is : " + blob);

        ReadChannel reader = blob.reader();
        InputStream inputStream = Channels.newInputStream(reader);

        byte[] content = IOUtils.toByteArray(inputStream);

        ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("whatever")
                                .build().toString())
                .body(resource);
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
//  public void deletePath(String folderId) {
//    try {
//      Path pathFolder = Paths.get(productFolder + folderId);
//      System.out.println("THE PATH : " + pathFolder);
//      if (Files.exists(pathFolder)) {
//        FileUtils.deleteDirectory(new File(String.valueOf(pathFolder)));
////                Files.delete(pathFolder);
//      }
//    } catch (Exception e) {
//      throw new RuntimeException(e);
//    }
//
//  }

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
}
