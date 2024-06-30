package com.it43.equicktrack.firebase;

import com.google.cloud.storage.BlobId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class FirebaseService {

    public String uploadImage(MultipartFile multipartFile, String filename) throws IOException {
        // TODO: upload the image through the firebase storage
        return "";
    }
}
