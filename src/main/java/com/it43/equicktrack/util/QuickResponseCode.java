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
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class QuickResponseCode {

    private static final String CHARSET = "UTF-8";
    private final FirebaseService firebaseService;
    public File generateQrCodeImage() throws IOException, WriterException {

        final String RANDOM_FILE_NAME = "%s-%s.jpeg"
                .formatted(
                        new SimpleDateFormat("MM-dd-yyyy_HHmmss")
                                .format(new Date())
                        , RandomStringUtils.randomAscii(8));
        final String QRCODE_DATA = RandomStringUtils.randomAlphabetic(18);

        // Create QR code
        BitMatrix bitMatrix = new QRCodeWriter().encode(QRCODE_DATA, BarcodeFormat.QR_CODE, 200, 200);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        File generatedQrcodeImageFile = new File(RANDOM_FILE_NAME);

        if (!ImageIO.write(bufferedImage, "jpeg", generatedQrcodeImageFile)) {
            throw new WriterException("Couldn't generate QR code");
        }

        return generatedQrcodeImageFile;
    }

}
