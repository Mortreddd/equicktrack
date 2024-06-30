package com.it43.equicktrack.util;

import com.it43.equicktrack.exception.ConvertMutipartFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@Service

public class ImageService {
    public File convertMultipartFileIntoFile(MultipartFile multipartFile, String fileName) throws Exception {
        File temporaryFile = new File(fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile)){
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (Exception e){
            throw new ConvertMutipartFileException(e.getMessage());
        }

        return temporaryFile;
    }
}
