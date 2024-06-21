package org.sevosmart.com.sevosmartbackend.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Value("${FIREBASE_PRIVATE_KEY:#{null}}")
    private String firebasePrivateKey;

    private Storage storage;

    @PostConstruct
    public void init() throws IOException {
        if (firebasePrivateKey == null) {
            throw new IllegalStateException("Firebase private key environment variable not set");
        }

        logger.info("Firebase private key environment variable is set.");

        byte[] decodedKey = Base64.getDecoder().decode(firebasePrivateKey);
        try (InputStream inputStream = new ByteArrayInputStream(decodedKey)) {
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("sevosmart-8ba91.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/sevosmart-8ba91.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String upload(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

        try {
            File file = this.convertToFile(multipartFile, fileName);
            String URL = this.uploadFile(file, fileName);
            Files.deleteIfExists(file.toPath());
            return URL;
        } catch (Exception e) {
            return "Image couldn't upload, Something went wrong: " + e.getMessage();
        }
    }

    public void delete(String fileUrl) {
        String fileName = extractFileName(fileUrl);
        BlobId blobId = BlobId.of("sevosmart-8ba91.appspot.com", fileName);
        storage.delete(blobId);
    }

    public String edit(MultipartFile newFile, String oldFileUrl) {
        delete(oldFileUrl);
        return upload(newFile);
    }

    private String extractFileName(String fileUrl) {
        String[] parts = fileUrl.split("/");
        String filePart = parts[parts.length - 1];
        return filePart.split("\\?")[0];
    }
}
