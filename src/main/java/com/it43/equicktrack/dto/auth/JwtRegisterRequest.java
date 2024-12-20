package com.it43.equicktrack.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it43.equicktrack.user.RoleName;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtRegisterRequest {

    @NotNull(message = "Full Name is required")
    @Size(min = 2, message = "Full Name must at least 3 more characters")
    private String fullName;
    @NotNull(message = "Email is required")
    @Email
    private String email;
    private String idNumber;
    @NotNull
    @Size(min = 8, message = "Password must have 8 characters")
    private String password;
    private RoleName role;

}
