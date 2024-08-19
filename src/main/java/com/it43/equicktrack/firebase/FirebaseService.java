package com.it43.equicktrack.firebase;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.StorageOptions;
import com.it43.equicktrack.exception.ConvertMultipartFileException;
import com.it43.equicktrack.exception.FirebaseFileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@RequiredArgsConstructor
@Service
@Slf4j
public class FirebaseService {
    private final ResourceLoader resourceLoader;
    private final FileService fileService;
//    FIRST PARAMETER  : FOLDER NAME OF FIREBASE
//    SECOND PARAMETER : THE FILE NAME
    private static String DOWNLOAD_URL = "https://storage.googleapis.com/equicktrack-api-service.appspot.com/%s/%s?alt=media";
    private static final String BUCKET_URL = "equicktrack-api-service.appspot.com";
    public String uploadFile(File file, FirebaseFolder firebaseFolder, String fileName) throws IOException, Exception {
        try {
            InputStream credentialsStream = resourceLoader.getResource("classpath:equicktrack-api-service-firebase-adminsdk.json").getInputStream();
            Credentials credentials = GoogleCredentials.fromStream(credentialsStream);

            BlobInfo blobInfo = BlobInfo.newBuilder(
                    BlobId.of(BUCKET_URL, getFirebaseFolder(firebaseFolder) + "/" + fileName))
                    .setContentType("image/png")
                    .build();

            StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService()
                    .create(blobInfo, Files.readAllBytes(file.toPath()));

            return getFirebaseFileUrl(firebaseFolder, fileName);
        } catch (IOException e) {
            log.error("Error uploading file to Firebase", e);
            throw new FirebaseFileUploadException("Unable to upload file into firebase");
        }

    }

    public String upload(MultipartFile multipartFile, FirebaseFolder firebaseFolder) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        File file = null;
        try {
            file = fileService.convertMultipartFileIntoFile(multipartFile, fileName);
            return uploadFile(file, firebaseFolder, fileName);
        } catch (Exception e) {
            log.error("Error uploading multipart file", e);
            throw new ConvertMultipartFileException("Failed to upload file");
        } finally {
            if (file != null && file.exists() && !file.delete()) {
                log.warn("Failed to delete temporary file: {}", file.getAbsolutePath());
            }
        }
    }

    private String getFirebaseFileUrl(FirebaseFolder firebaseFolder, String fileName){
        return switch(firebaseFolder){
            case AVATAR -> String.format(DOWNLOAD_URL, "avatars", URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            case QR_IMAGE -> String.format(DOWNLOAD_URL, "qr-images", URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            case EQUIPMENT -> String.format(DOWNLOAD_URL, "equipments", URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        };
    }

    private String getFirebaseFolder(FirebaseFolder firebaseFolder){
        return switch(firebaseFolder){
            case AVATAR -> "avatars";
            case EQUIPMENT -> "equipments";
            case QR_IMAGE -> "qr-images";
        };
    }
}
