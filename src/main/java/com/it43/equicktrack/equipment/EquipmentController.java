package com.it43.equicktrack.equipment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api/v1/equipments")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<Equipment>> getEquipments(){
        return ResponseEntity.ok().body(equipmentService.getEquipments());
    }

    @GetMapping(path = "/{equipment_id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable("equipment_id") Long _equipmentId){
        Equipment equipment = equipmentService.getEquipmentById(_equipmentId);

        return ResponseEntity.status(HttpStatus.OK).body(equipment);

    }

    @GetMapping(path = "/qrcode/{qrcode}")
    public ResponseEntity<Equipment> findEquipmentByQrcodeData(@PathVariable("qrcode") String _qrcode){
        Optional<Equipment> equipment = equipmentService.getEquipmentByQrcodeData(_qrcode);

        return equipment.map(_equipment ->
                ResponseEntity.ok().body(_equipment)
        ).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createEquipment(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.createEquipment(file));
    }
//
//    @PostMapping(path = "/create")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment){
//        return ResponseEntity.ok().body(equipmentService.createEquipment(equipment));
//    }

}
