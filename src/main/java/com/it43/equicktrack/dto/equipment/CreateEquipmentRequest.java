package com.it43.equicktrack.dto.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@JsonIgnoreProperties( ignoreUnknown = true )
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEquipmentRequest {
    @NotNull
    private String name;
    private String description = null;
    @NotNull
    private MultipartFile equipmentImage = null;
}
