package com.it43.equicktrack.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.it43.equicktrack.firebase.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuickResponseCode {

    private static final String CHARSET = "UTF-8";
    private final FirebaseService firebaseService;
    private final String DIRECTORY = "storage/images/qr-images";
    private final FileUtil fileUtil;

    public File generateQrCodeImage(String fileName, String qrcodeData) throws IOException, WriterException {

        // Generate random file name with a .png extension
        final String RANDOM_FILE_NAME = fileUtil.generateRandomFileName(fileName, "png");

        // Create the QR code
        BitMatrix bitMatrix = new QRCodeWriter().encode(qrcodeData, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Ensure the directory exists
        File directory = new File(DIRECTORY);
        if (!directory.exists()) {
            Files.createDirectories(directory.toPath());
        }

        // Save the QR code image to the directory
        File generatedQrcodeImageFile = new File(directory, RANDOM_FILE_NAME);
        if (!ImageIO.write(bufferedImage, "png", generatedQrcodeImageFile)) {
            throw new WriterException("Couldn't generate QR code");
        }

        return generatedQrcodeImageFile;
    }

    public String generateQrcodeData() {
        return UUID.randomUUID().toString();
    }

    public String getDirectory() {
        return DIRECTORY;
    }

    public void deleteQrcodeImage(String directory, String fileName){
        File file = new File(directory + "/" + fileName);
        file.delete();
    }
}
