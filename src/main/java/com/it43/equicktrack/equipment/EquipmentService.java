package com.it43.equicktrack.equipment;

import com.it43.equicktrack.exception.ConvertMultipartFileException;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.firebase.FirebaseService;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.QuickResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        return equipmentRepository.findById(_id).orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
    }


    public Optional<Equipment> getEquipmentByQrcodeData(String qrcodeData){
        return equipmentRepository.findEquipmentByQrcodeData(qrcodeData);
    }


    public String createEquipment(MultipartFile file) throws IOException {
        try{
            return firebaseService.upload(file);
        } catch( Exception error) {
            throw new ConvertMultipartFileException("File can't be uploaded");
        }
    }
}
