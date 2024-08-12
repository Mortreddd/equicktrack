package com.it43.equicktrack.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateTransactionRequestDTO {
    private Long userId;
    private Long equipmentId;
    private String purpose = null;
    private LocalDateTime borrowData = LocalDateTime.now();
    private LocalDateTime returnDate = LocalDateTime.now().plusHours(1);

}
