package com.it43.equicktrack.equipment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public List<Equipment> getEquipments(){
        return equipmentRepository.findAll();
    }

    public Optional<Equipment> getEquipmentById(Long _id){
        return equipmentRepository.findById(_id);
    }

    public Equipment createEquipment(Equipment _equipment){
        return equipmentRepository.save(_equipment);
    }

    public Optional<Equipment> getEquipmentByQrcode(String qrcode){
        return equipmentRepository.findEquipmentByQrcode(qrcode);
    }
}
