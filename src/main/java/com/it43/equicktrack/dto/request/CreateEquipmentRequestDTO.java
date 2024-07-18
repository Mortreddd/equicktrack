package com.it43.equicktrack.dto.request;

import com.it43.equicktrack.equipment.Equipment;
import com.it43.equicktrack.firebase.FirebaseFolder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEquipmentRequestDTO {
    private String name;
    private String qrcodeData = null;
    private MultipartFile qrcodeImage = null;
    private String description = null;
    private MultipartFile equipmentImage = null;

}
