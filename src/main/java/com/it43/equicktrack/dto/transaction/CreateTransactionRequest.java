package com.it43.equicktrack.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTransactionRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long equipmentId;
    private String purpose;
    @NotNull
    private String borrowDate;
    @NotNull
    private String returnDate;
}
