package com.it43.equicktrack.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it43.equicktrack.equipment.Remark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApprovedTransactionRequest {

    private Boolean available;
    private Remark remark;
    private String message;
}
