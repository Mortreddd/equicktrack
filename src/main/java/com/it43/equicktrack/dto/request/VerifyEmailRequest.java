package com.it43.equicktrack.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailRequest {
    @NotNull
    @NotBlank
    @Email
    private String oldEmail;
    @NotNull
    @NotBlank
    @Email
    private String newEmail;
}
