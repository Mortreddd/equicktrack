package com.it43.equicktrack.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuickResponseCode {

    private static final String FOLDER_PATH = "src/main/resources/static/images/qrcodes/";
    private static final String CHARSET = "UTF-8";

    public String generateQrCode(String data, String fileName) throws IOException, WriterException {
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                new String(data.getBytes(CHARSET), CHARSET),
                BarcodeFormat.QR_CODE, 200, 200, hints);

        Path path = Paths.get(FOLDER_PATH + fileName + ".png");
        Files.createDirectories(path.getParent());
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        // directory of the created qr code
        return path.toString();
    }
}
