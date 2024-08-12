package com.it43.equicktrack.firebase;

import com.it43.equicktrack.exception.ConvertMultipartFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@Service
public class FileService {
    public File convertMultipartFileIntoFile(MultipartFile multipartFile, String fileName) throws Exception {
        File temporaryFile = new File(fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile)){
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (Exception e){
            throw new ConvertMultipartFileException(e.getMessage());
        }

        return temporaryFile;
    }

    private String getExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
