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
public class CreateTransactionRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long equipmentId;

    private String purpose = null;
    @NotNull
    private LocalDateTime borrowDate;
    @NotNull
    private LocalDateTime returnDate;
}
