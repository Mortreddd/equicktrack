package com.it43.equicktrack.util;

import com.it43.equicktrack.exception.firebase.ConvertMultipartFileException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;


@Component
public class FileUtil {


    public boolean deleteFile(String fileDirectory, String fileName) {
        File file = new File(fileDirectory + "/" + fileName);
        return file.delete();
    }

    public String generateRandomFileName() {
        return "%s-%s"
                .formatted(
                        new SimpleDateFormat("MM-dd-yyyy_HHmmss").format(new Date()),
                        RandomStringUtils.randomNumeric(10));
    }

    public String generateRandomFileName(String fileName) {

        return "%s-%s-%s"
                .formatted(
                        new SimpleDateFormat("MM-dd-yyyy_HHmmss").format(new Date()),
                        RandomStringUtils.randomNumeric(10),
                        fileName.trim());
    }

    public String generateRandomFileName(String fileName, String fileExtension) {

        return "%s-%s-%s.%s"
                .formatted(
                        new SimpleDateFormat("MM-dd-yyyy_HHmmss").format(new Date()),
                        RandomStringUtils.randomNumeric(10),
                        fileName.trim(),
                        fileExtension);
    }

    public File convertMultipartFileIntoFile(MultipartFile multipartFile, String fileName) throws Exception {
        File temporaryFile = new File(fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile)){
            fileOutputStream.write(multipartFile.getBytes());
        } catch (Exception e){
            throw new ConvertMultipartFileException(e.getMessage());
        }

        return temporaryFile;
    }

    public String getExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String encodeFileToBase64(Path filePath) throws IOException {
        byte[] readAllBytes = Files.readAllBytes(filePath);
        return Base64.getEncoder().encodeToString(readAllBytes);
    }

}
