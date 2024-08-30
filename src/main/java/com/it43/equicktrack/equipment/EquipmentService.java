package com.it43.equicktrack.equipment;

import com.it43.equicktrack.dto.request.CreateEquipmentRequestDTO;
import com.it43.equicktrack.exception.ConvertMultipartFileException;
import com.it43.equicktrack.exception.FirebaseFileUploadException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.firebase.FirebaseFolder;
import com.it43.equicktrack.firebase.FirebaseService;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.FileUtil;
import com.it43.equicktrack.util.QuickResponseCode;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final QuickResponseCode quickResponseCode;
    private final FirebaseService firebaseService;
    private final FileUtil fileUtil;

    public List<Equipment> getEquipments() {
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(Long _id) {
        return equipmentRepository.findById(_id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
    }


    public Equipment getEquipmentByQrcodeData(String qrcodeData) {
        return equipmentRepository.findEquipmentByQrcodeData(qrcodeData);
    }


    public Equipment createEquipment(CreateEquipmentRequestDTO createEquipmentRequestDTO) throws IOException {
        try {

            File qrcodeFile = quickResponseCode.generateQrCodeImage(createEquipmentRequestDTO.getName());
            MultipartFile equipmentFile = createEquipmentRequestDTO.getEquipmentImage();
            String equipmentDownloadUrl = firebaseService.uploadMultipartFile(equipmentFile, FirebaseFolder.EQUIPMENT);
            String qrcodeDownloadUrl = firebaseService.uploadFile(qrcodeFile, FirebaseFolder.QR_IMAGE, qrcodeFile.getName());

            Equipment equipment = Equipment.builder()
                    .name(createEquipmentRequestDTO.getName())
                    .qrcodeData(quickResponseCode.generateQrcodeData())
                    .available(true)
                    .qrcodeImage(qrcodeDownloadUrl)
                    .description(createEquipmentRequestDTO.getDescription())
                    .equipmentImage(equipmentDownloadUrl)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            equipmentRepository.save(equipment);
            return equipment;
        } catch (Exception error) {
            throw new ConvertMultipartFileException("File can't be uploaded");
        }
    }

    public boolean deleteEquipmentById(Long equipmentId) throws FirebaseFileUploadException, IOException {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));


        String qrcodeImage = firebaseService.extractFileFromFirebaseUrl(equipment.getQrcodeImage());
        if(!fileUtil.deleteFile("storage/images" , qrcodeImage)) {
            return false;
        }

        if(!firebaseService.delete(equipment.getEquipmentImage())) {
            return false;
        }

        if(!firebaseService.delete(equipment.getQrcodeImage())){
            return false;
        }
        equipmentRepository.deleteById(equipmentId);

        return true;
    }


}
