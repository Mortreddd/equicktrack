package com.it43.equicktrack.firebase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class FirebaseService {

    public void uploadImage(MultipartFile multipartFile, String fileName) throws IOException {
        //
    }
}
