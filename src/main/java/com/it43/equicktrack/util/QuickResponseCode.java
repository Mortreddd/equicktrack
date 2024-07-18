package com.it43.equicktrack.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.it43.equicktrack.firebase.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class QuickResponseCode {

    private static final String CHARSET = "UTF-8";
    private final FirebaseService firebaseService;
    public BufferedImage generateQrCodeImage(String data) throws IOException, WriterException {
        BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, 200, 200);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
