package com.it43.equicktrack.equipment;

import com.it43.equicktrack.dto.request.CreateEquipmentRequestDTO;
import com.it43.equicktrack.exception.FirebaseFileUploadException;
import com.it43.equicktrack.transaction.TransactionService;
import com.it43.equicktrack.util.QuickResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api/v1/equipments")
public class EquipmentController {

    private final QuickResponseCode quickResponseCode;
    private final EquipmentService equipmentService;
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Equipment>> getEquipments(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(equipmentService.getEquipments());
    }

    @GetMapping(path = "/{equipment_id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable("equipment_id") Long _equipmentId){
        Equipment equipment = equipmentService.getEquipmentById(_equipmentId);

        return ResponseEntity.status(HttpStatus.OK).body(equipment);

    }

    @GetMapping(path = "/qrcode/{qrcode}")
    public ResponseEntity<Equipment> findEquipmentByQrcodeData(@PathVariable("qrcode") String _qrcode){
        Equipment equipment = equipmentService.getEquipmentByQrcodeData(_qrcode);
        return ResponseEntity.status(HttpStatus.OK)
                .body(equipment);
    }

    @PostMapping(path = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Equipment> createEquipment(@ModelAttribute CreateEquipmentRequestDTO equipment) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(equipmentService.createEquipment(equipment));
    }

    @GetMapping(path = "/{equipmentId}/transactions")
    public ResponseEntity<Equipment> getTransactionsByEquipment(@PathVariable("equipmentId") Long equipmentId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.getTransactionsByEquipment(equipmentId));
    }

    @DeleteMapping(path = "/{equipmentId}/delete")
    public ResponseEntity deleteEquipmentById(@PathVariable("equipmentId") Long equipmentId) throws FirebaseFileUploadException, IOException {
        if(equipmentService.deleteEquipmentById(equipmentId)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }
}
