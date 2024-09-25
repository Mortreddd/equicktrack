package com.it43.equicktrack.dto.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it43.equicktrack.equipment.Remark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateEquipmentRequest {

    private String name;
    private String description;
    private MultipartFile equipmentImage;
    private boolean available;
    private Remark remark;

}