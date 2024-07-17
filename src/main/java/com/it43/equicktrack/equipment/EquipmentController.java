package com.it43.equicktrack.equipment;

import com.google.zxing.WriterException;
import com.it43.equicktrack.dto.request.CreateEquipmentRequestDTO;
import com.it43.equicktrack.firebase.FirebaseFolder;
import com.it43.equicktrack.util.QuickResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    private final QuickResponseCode quickResponseCode;
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

    @GetMapping(path = "/qrcode/generate")
    public ResponseEntity<byte[]> generateQrcodeImage() throws IOException, WriterException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Content-Type", MediaType.IMAGE_PNG_VALUE)
                .body(equipmentService.generateQrcode());
    }


    @PostMapping(path = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Equipment> createEquipment(@ModelAttribute CreateEquipmentRequestDTO equipment) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(equipmentService.createEquipment(equipment));
    }

}
