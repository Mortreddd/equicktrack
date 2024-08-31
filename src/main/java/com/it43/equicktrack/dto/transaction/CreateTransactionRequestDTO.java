package com.it43.equicktrack.dto.transaction;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Long userId;
    @NotNull
    private Long equipmentId;
    private String purpose = null;
//    @NotNull
//    private LocalDateTime borrowData;
//    @NotNull
//    private LocalDateTime returnDate;
}
