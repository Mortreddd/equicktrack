package com.it43.equicktrack.equipment;

import com.google.zxing.WriterException;
import com.it43.equicktrack.dto.equipment.CreateEquipmentRequest;
import com.it43.equicktrack.dto.equipment.EditInventoryRequest;
import com.it43.equicktrack.dto.equipment.EquipmentDTO;
import com.it43.equicktrack.dto.equipment.UpdateEquipmentRequest;
import com.it43.equicktrack.dto.response.Response;
import com.it43.equicktrack.dto.transaction.TransactionDTO;
import com.it43.equicktrack.exception.EmailMessageException;
import com.it43.equicktrack.exception.auth.EmailExistsException;
import com.it43.equicktrack.exception.firebase.FirebaseFileUploadException;
import com.it43.equicktrack.transaction.TransactionService;
import com.it43.equicktrack.util.QuickResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api/v1/equipments")
public class EquipmentController {

    private final QuickResponseCode quickResponseCode;
    private final EquipmentService equipmentService;
    private final TransactionService transactionService;


    @GetMapping
    public ResponseEntity<Page<Equipment>> getEquipments(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(equipmentService.getEquipments(pageNo, pageSize));
    }

    @GetMapping(path = "/{equipment_id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable("equipment_id") Long _equipmentId){
        Equipment equipment = equipmentService.getEquipmentById(_equipmentId);
        return ResponseEntity.status(HttpStatus.OK).body(equipment);
    }

    @GetMapping(path = "/qrcode/{qrcode}")
    public ResponseEntity<EquipmentDTO> findEquipmentByQrcodeData(@PathVariable("qrcode") String _qrcode){
        EquipmentDTO equipment = equipmentService.getEquipmentByQrcodeData(_qrcode);
        return ResponseEntity.status(HttpStatus.OK)
                .body(equipment);
    }

    @GetMapping(path = "/qrcode/generate")
    public ResponseEntity<String> generateQrcode() throws IOException, WriterException {
        String qrcodeFileUrl = equipmentService.generateQrcode();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(qrcodeFileUrl);
    }

    @GetMapping(path = "/qrcode/storage/{fileName}")
    public ResponseEntity<Resource> getQrcodeFile(@PathVariable("fileName") String fileName) throws FileNotFoundException, MalformedURLException {

        Path filePath = Paths.get("storage/images/qr-images").resolve(fileName);

        if(!Files.exists(filePath)) {
            throw new FileNotFoundException("Qrcode File not found");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(new UrlResource(filePath.toUri()));
    }

    @PostMapping(path = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Equipment> createEquipment(@Validated @ModelAttribute CreateEquipmentRequest equipment) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(equipmentService.createEquipment(equipment));
    }

    @GetMapping(path = "/{equipmentId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByEquipment(@PathVariable("equipmentId") Long equipmentId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.getTransactionsByEquipment(equipmentId));
    }

//    Modifying the equipment
    @PatchMapping(path = "/{equipmentId}/update")
    public ResponseEntity<Equipment> updateEquipmentById(
            @PathVariable("equipmentId") Long equipmentId,
            @Validated @ModelAttribute UpdateEquipmentRequest updateEquipmentRequest
    ) throws FirebaseFileUploadException, IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(equipmentService.updateEquipment(equipmentId, updateEquipmentRequest));
    }

//    Modifying the status of equipment
    @PatchMapping(path = "/{equipmentId}/update/status")
    public ResponseEntity<Equipment> updateEquipmentStatusById(
            @PathVariable("equipmentId") Long equipmentId,
            @RequestBody EditInventoryRequest editInventoryRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(equipmentService.updateEquipmentStatus(equipmentId, editInventoryRequest));
    }
    @DeleteMapping(path = "/{equipmentId}/delete")
    public ResponseEntity<Response> deleteEquipmentById(@PathVariable("equipmentId") Long equipmentId) throws FirebaseFileUploadException, IOException {
        if(equipmentService.deleteEquipmentById(equipmentId)){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.builder()
                            .code(200)
                            .message("Successfully deleted equipment")
                            .build()
                    );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(path = "/{equipmentId}/occupied")
    public ResponseEntity<EquipmentDTO> getOccupiedEquipment(
            @PathVariable("equipmentId") Long equipmentId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(equipmentService.getOccupiedEquipmentById(equipmentId));
    }

//    @GetMapping(path = "/available")
//    public ResponseEntity<Page<Equipment>> getAvailableEquipments(
//            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
//            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
//    ) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(equipmentService.getAvailableEquipments(pageNo, pageSize));
//    }
//
//    @GetMapping(path = "/unavailable")
//    public ResponseEntity<Page<Equipment>> getUnavailableEquipments(
//            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
//            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
//    ) {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(equipmentService.getUnavailableEquipments(pageNo, pageSize));
//    }
}
