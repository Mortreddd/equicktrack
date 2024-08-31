package com.it43.equicktrack.dto.request;

import com.it43.equicktrack.equipment.Equipment;
import com.it43.equicktrack.firebase.FirebaseFolder;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String name;
    private String description = null;

    @NotNull
    private MultipartFile equipmentImage = null;
}
