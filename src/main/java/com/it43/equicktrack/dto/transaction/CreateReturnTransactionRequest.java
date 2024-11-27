package com.it43.equicktrack.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it43.equicktrack.equipment.Remark;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateReturnTransactionRequest {


    @NotNull
    private Long userId;

    @NotNull
    private Long equipmentId;

    @NotNull
    private Remark remark;

    private MultipartFile conditionImage;

    private MultipartFile  returnProofImage;

}
