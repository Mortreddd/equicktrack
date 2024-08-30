package com.it43.equicktrack.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtRegisterRequestDTO {
    @NotBlank
    @NotNull(message = "Full Name is required")
    @Min(value = 2, message = "Full Name must at least 3 more characters")
    private String fullName;
    @NotBlank
    @NotNull(message = "Email is required")
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Min(value = 8, message = "Password must have 8 characters")
    private String password;
    private Integer roleId = 3;

}
