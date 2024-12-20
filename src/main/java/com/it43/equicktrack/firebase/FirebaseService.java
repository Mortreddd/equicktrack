package com.it43.equicktrack.firebase;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import com.it43.equicktrack.configuration.FirebaseConfiguration;
import com.it43.equicktrack.exception.firebase.ConvertMultipartFileException;
import com.it43.equicktrack.exception.firebase.FirebaseFileUploadException;
import com.it43.equicktrack.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@RequiredArgsConstructor
@Service
@Slf4j
public class FirebaseService {
    private final ResourceLoader resourceLoader;
    private final FileUtil fileUtil;
//    FIRST PARAMETER  : FOLDER NAME OF FIREBASE
//    SECOND PARAMETER : THE FILE NAME
    @Value("${firebase.storage.download-url}")
    private String DOWNLOAD_URL;

    @Value("${firebase.storage.bucket-url}")
    private String BUCKET_URL;

    @Value("${firebase.storage.access-url}")
    private String ACCESS_URL;

    private final Environment environment;
    private final FirebaseConfiguration firebaseConfiguration;


    public String uploadFile(File file, FirebaseFolder firebaseFolder, String fileName) throws IOException, Exception {
        try {
            InputStream credentialsStream = firebaseConfiguration.getFirebaseCredentialsStream();
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


//            return getFirebaseFileUrl(firebaseFolder, fileName);
            return getFirebaseFileUrl(firebaseFolder, fileName);
        } catch (IOException e) {

            log.error("Error uploading file to Firebase", e);
            throw new FirebaseFileUploadException("Unable to upload file into firebase");
        }

    }

    public String uploadMultipartFile(MultipartFile multipartFile, FirebaseFolder firebaseFolder) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String randomFileName = fileUtil.generateRandomFileName(originalFileName);
        File file = null;
        try {
            file = fileUtil.convertMultipartFileIntoFile(multipartFile, randomFileName);
            return uploadFile(file, firebaseFolder, randomFileName);
        } catch (Exception e) {
            log.error("Error uploading multipart file", e);
            throw new ConvertMultipartFileException("Failed to upload file");
        } finally {
            if (file != null && file.exists() && !file.delete()) {
                log.warn("Failed to delete temporary file: {}", file.getAbsolutePath());
            }
        }
    }


    public boolean delete(String filePath) throws IOException, FirebaseFileUploadException {
        if(filePath == null) return false;
        try {
            Bucket bucket = StorageClient.getInstance().bucket(BUCKET_URL);
            String extractedFile = extractFileFromFirebaseUrl(filePath);
            Blob blob = bucket.get(extractedFile);

            if(blob == null) {
                throw new FirebaseFileUploadException("Unable to find the file using path");
            }

            return blob.delete();
        } catch ( Exception e ) {
            log.error("Error Message: {}", e);
            throw new FirebaseFileUploadException("Unable to delete image of equipment");
        }
    }


    private String getFirebaseFileUrl(FirebaseFolder firebaseFolder, String fileName){
        return String.format(ACCESS_URL, URLEncoder.encode(getFirebaseFolder(firebaseFolder) + "/" + fileName, StandardCharsets.UTF_8));
    }

    private String getFirebaseFolder(FirebaseFolder firebaseFolder){
        return switch(firebaseFolder){
            case AVATAR -> "avatars";
            case EQUIPMENT -> "equipments";
            case QR_IMAGE -> "qr-images";
            case CONDITION -> "conditions";
            case PROOF -> "proofs";
        };
    }

//    returns the file along with its folder from firebase ex. equipments/Projector.png
    public String extractFileFromFirebaseUrl(String fileUrl) {
        String[] splittedFile = fileUrl.split("/");
        return URLDecoder.decode(splittedFile[7].replace("?alt=media", ""), StandardCharsets.UTF_8);
    }

}
