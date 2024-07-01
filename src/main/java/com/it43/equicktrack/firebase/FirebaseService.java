package com.it43.equicktrack.firebase;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.it43.equicktrack.exception.ConvertMultipartFileException;
import com.it43.equicktrack.util.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
//
//        https://console.firebase.google.com/u/0/project/equicktrack-api-service/storage/equicktrack-api-service.appspot.com/files
@RequiredArgsConstructor
@Service
public class FirebaseService {
    private final ResourceLoader resourceLoader;
    private final FileService fileService;
    public String uploadFile(File file, String fileName) throws IOException {
        final String DOWNLOAD_URL = "https://console.firebase.google.com/u/0/project/equicktrack-api-service/storage/equicktrack-api-service.appspot.com/files?alt=media";
        final InputStream CREDENTIALS_FILE_PATH = resourceLoader
                .getResource("equicktrack-api-service-firebase-adminsdk.json")
                .getInputStream();

        BlobInfo blobInfo = BlobInfo.newBuilder(
                BlobId.of("equicktrack-api-service.appspot.com", fileName))
                .setContentType("media")
                .build();

        Credentials credentials = GoogleCredentials.fromStream(CREDENTIALS_FILE_PATH);
        Blob storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService()
                .create(blobInfo, Files.readAllBytes(file.toPath()));

        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    public String upload(MultipartFile multipartFile) throws IOException{
        try{
            String fileName = multipartFile.getOriginalFilename();
            File file = fileService.convertMultipartFileIntoFile(multipartFile, fileName);
            String url = uploadFile(file, fileName);
            if(!file.delete()){
                return "File couldn't be deleted";
            }
            return url;

        } catch (Exception e) {
            throw new ConvertMultipartFileException(e.getMessage());
        }
    }
}
