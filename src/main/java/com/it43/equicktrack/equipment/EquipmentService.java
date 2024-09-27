package com.it43.equicktrack.equipment;

import com.google.zxing.WriterException;
import com.it43.equicktrack.dto.equipment.CreateEquipmentRequest;
import com.it43.equicktrack.dto.equipment.EquipmentDTO;
import com.it43.equicktrack.dto.equipment.UpdateEquipmentRequest;
import com.it43.equicktrack.exception.ConvertMultipartFileException;
import com.it43.equicktrack.exception.FirebaseFileUploadException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.firebase.FirebaseFolder;
import com.it43.equicktrack.firebase.FirebaseService;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.DateUtilities;
import com.it43.equicktrack.util.FileUtil;
import com.it43.equicktrack.util.QuickResponseCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
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
        return equipmentRepository.findByQrcodeData(qrcodeData.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
    }

    public Equipment createEquipment(CreateEquipmentRequest createEquipmentRequest) throws IOException {
        try {
            String qrcodeData = quickResponseCode.generateQrcodeData();
            MultipartFile equipmentFile = createEquipmentRequest.getEquipmentImage();
            File qrcodeFile = quickResponseCode.generateQrCodeImage(createEquipmentRequest.getName(), qrcodeData);
            String equipmentDownloadUrl = firebaseService.uploadMultipartFile(equipmentFile, FirebaseFolder.EQUIPMENT);
            String qrcodeDownloadUrl = firebaseService.uploadFile(qrcodeFile, FirebaseFolder.QR_IMAGE, qrcodeFile.getName());

            Equipment equipment = Equipment.builder()
                    .name(createEquipmentRequest.getName())
                    .qrcodeData(qrcodeData)
                    .available(true)
                    .qrcodeImage(qrcodeDownloadUrl)
                    .description(createEquipmentRequest.getDescription())
                    .serialNumber(createEquipmentRequest.getSerialNumber())
                    .equipmentImage(equipmentDownloadUrl)
                    .remark(Remark.GOOD_CONDITION)
                    .createdAt(DateUtilities.now())
                    .updatedAt(DateUtilities.now())
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
        fileUtil.deleteFile("storage/images" , qrcodeImage);

        if(!firebaseService.delete(equipment.getEquipmentImage())) {
            return false;
        }

        if(!firebaseService.delete(equipment.getQrcodeImage())){
            return false;
        }
        equipmentRepository.deleteById(equipmentId);

        return true;
    }

    @Transactional
    public Equipment updateEquipment(Long equipmentId, UpdateEquipmentRequest updateEquipmentRequest) throws FirebaseFileUploadException, IOException {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

        if (updateEquipmentRequest.getName() != null) {
            equipment.setName(updateEquipmentRequest.getName());
        }

        if (updateEquipmentRequest.getDescription() != null) {
            equipment.setDescription(updateEquipmentRequest.getDescription());
        }

        if (updateEquipmentRequest.getEquipmentImage() != null) {
            firebaseService.delete(equipment.getEquipmentImage());
            String newEquipmentImage = firebaseService.uploadMultipartFile(updateEquipmentRequest.getEquipmentImage(), FirebaseFolder.EQUIPMENT);
            equipment.setEquipmentImage(newEquipmentImage);
        }

        equipment.setAvailable(updateEquipmentRequest.isAvailable());
        equipment.setUpdatedAt(DateUtilities.now());
        return equipmentRepository.save(equipment);
    }


    public Equipment getBySerialNumber(String serialNumber) {
        return equipmentRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
    }

    public String generateQrcode() throws IOException, WriterException {
        final String QRCODE_DATA = quickResponseCode.generateQrcodeData();
        String fileName = quickResponseCode.generateQrCodeImage(fileUtil.generateRandomFileName(), generateQrcode()).getName();
        Path filePath = Paths.get("storage/images/qr-images").resolve(fileName);

        if(Files.exists(filePath)) {
//            String fileBase64 = fileUtil.encodeFileToBase64(filePath);
//          Returns the String value of file with certain endpoint
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/equipments/qrcode/storage/" + fileName)
                    .toUriString();
//            return new UrlResource();
        }

        log.error("Qrcode not found path: {}", filePath.toString());
        Files.delete(filePath);
        throw new FileNotFoundException("Qrcode not found");

    }

    public EquipmentDTO getOccupiedEquipmentById(Long equipmentId) {
        return getOccupiedEquipments()
                .stream()
                .filter(( _equipment ) -> Objects.equals(_equipment.getId(), equipmentId))
                .map((_equipment) -> new EquipmentDTO(
                        _equipment.getId(),
                        _equipment.getName(),
                        _equipment.getDescription(),
                        _equipment.getQrcodeData(),
                        _equipment.getQrcodeImage(),
                        _equipment.getSerialNumber(),
                        _equipment.getEquipmentImage(),
                        _equipment.getRemark(),
                        _equipment.isAvailable(),
                        _equipment.getCreatedAt(),
                        _equipment.getUpdatedAt(),
                        _equipment.getTransactions()
                    )
                ).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Equipment is not currently borrowed"));
    }

    public List<Equipment> getOccupiedEquipments() {
        return equipmentRepository.findAll()
                .stream()
                .filter((_equipment) ->
                        _equipment.getTransactions()
                                .stream()
                                .anyMatch((transaction) -> transaction.getReturnedAt() == null)
                        && !_equipment.isAvailable()
                ).toList();

    }

}
