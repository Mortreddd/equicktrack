package com.it43.equicktrack.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsVerificationRequest {
    @NotNull
    @NotBlank
    private String contactNumber;

    @NotNull
    private Long userId;

}
