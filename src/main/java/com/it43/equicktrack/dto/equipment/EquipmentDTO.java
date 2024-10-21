package com.it43.equicktrack.dto.equipment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it43.equicktrack.equipment.Remark;
import com.it43.equicktrack.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentDTO {

    private Long id;
    private String name;
    private String description;
    private String qrcodeData;

    private String qrcodeImage;
    private String equipmentImage;
    private Remark remark = Remark.GOOD_CONDITION;
    private boolean available = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Transaction> transactions;

}
