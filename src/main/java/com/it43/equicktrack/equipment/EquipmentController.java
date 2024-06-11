package com.it43.equicktrack.equipment;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api/v1/equipments", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE})
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<Equipment>> getEquipments(){
        return ResponseEntity.ok().body(equipmentService.getEquipments());
    }

    @GetMapping(path = "/{equipment_id}")
    public ResponseEntity<Equipment> findEquipment(@PathVariable("equipment_id") Long _equipmentId){
        Optional<Equipment> equipment = equipmentService.getEquipmentById(_equipmentId);
        return equipment.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

    }

    @PostMapping(path = "/create")
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment){
        return ResponseEntity.ok().body(equipmentService.createEquipment(equipment));
    }

    @GetMapping(path = "/{qrcode}")
    public ResponseEntity<Equipment> findEquipmentByQr(@PathVariable("qrcode") String _qrcode){
        Optional<Equipment> equipment = equipmentService.getEquipmentByQrCode(_qrcode);

        return equipment.map(_equipment -> ResponseEntity.ok().body(_equipment))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
