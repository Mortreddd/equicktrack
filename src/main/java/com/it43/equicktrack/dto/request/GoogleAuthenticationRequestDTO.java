package com.it43.equicktrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleAuthenticationRequestDTO {
    @NotBlank
    @NotNull
    private String uid;
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    private String displayName;
    @NotNull
    @NotBlank
    private String photoUrl;
}
