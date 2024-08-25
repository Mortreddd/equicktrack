package com.it43.equicktrack.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.it43.equicktrack.firebase.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuickResponseCode {

    private static final String CHARSET = "UTF-8";
    private final FirebaseService firebaseService;
    private final String DIRECTORY = "storage/images/qr-images";

    public File generateQrCodeImage(String fileName) throws IOException, WriterException {

        // Generate random file name with a .png extension
        final String RANDOM_FILE_NAME = "%s-%s-%s.png"
                .formatted(
                        fileName,
                        new SimpleDateFormat("MM-dd-yyyy_HHmmss").format(new Date()),
                        RandomStringUtils.randomNumeric(10));

        final String QRCODE_DATA = UUID.randomUUID().toString();

        // Create the QR code
        BitMatrix bitMatrix = new QRCodeWriter().encode(QRCODE_DATA, BarcodeFormat.QR_CODE, 200, 200);
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
}
