package com.it43.equicktrack.dto.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it43.equicktrack.equipment.Remark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditInventoryRequest {
    private Remark remark;
    private boolean available;
}
