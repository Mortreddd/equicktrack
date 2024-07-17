package com.it43.equicktrack.equipment;

import com.google.zxing.WriterException;
import com.it43.equicktrack.dto.request.CreateEquipmentRequestDTO;
import com.it43.equicktrack.exception.ConvertMultipartFileException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.firebase.FirebaseFolder;
import com.it43.equicktrack.firebase.FirebaseService;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.QuickResponseCode;
import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final QuickResponseCode quickResponseCode;
    private final FirebaseService firebaseService;

    public List<Equipment> getEquipments(){
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(Long _id){
        return equipmentRepository.findById(_id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
    }


    public Optional<Equipment> getEquipmentByQrcodeData(String qrcodeData){
        return equipmentRepository.findEquipmentByQrcodeData(qrcodeData);
    }


    public Equipment createEquipment(CreateEquipmentRequestDTO createEquipmentRequestDTO) throws IOException {
        try{
            MultipartFile equipmentFile = createEquipmentRequestDTO.getEquipmentImage();
            MultipartFile qrcodeFile = createEquipmentRequestDTO.getQrcodeImage();
            String equipmentDownloadUrl = firebaseService.upload(equipmentFile, FirebaseFolder.EQUIPMENT);
            String qrcodeDownloadUrl = firebaseService.upload(qrcodeFile, FirebaseFolder.QR_IMAGE);
            Equipment equipment = Equipment.builder()
                    .name(createEquipmentRequestDTO.getName())
                    .qrcodeData(createEquipmentRequestDTO.getQrcodeData())
                    .available(true)
                    .qrcodeImage(qrcodeDownloadUrl)
                    .description(createEquipmentRequestDTO.getDescription())
                    .equipmentImage(equipmentDownloadUrl)
                    .build(); 

            equipmentRepository.save(equipment);
            return equipment;
        } catch( Exception error) {
            throw new ConvertMultipartFileException("File can't be uploaded");
        }
    }


    public byte[] generateQrcode() throws IOException, WriterException {

        BufferedImage qrcodeImage = quickResponseCode.generateQrCodeImage("projector");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrcodeImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        return imageBytes;

    }
}
