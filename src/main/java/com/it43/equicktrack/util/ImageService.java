package com.it43.equicktrack.util;

import com.it43.equicktrack.exception.ConvertMultipartFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ImageService {
    public File convertMultipartFileIntoFile(
            MultipartFile multipartFile,
            String filename
    ) throws IOException {
        File temporaryFile = new File(filename);
        try( FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile) ){
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            throw new ConvertMultipartFileException(e.getMessage());
        }

        return temporaryFile;
    }
}
