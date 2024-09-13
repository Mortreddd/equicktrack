package com.it43.equicktrack.dto.request;

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
public class GoogleAuthenticationRequestDTO {

    @NotNull
    private String uid;
    @NotNull
    private String email;
    @NotNull
    private String displayName;
    @NotNull
    private String photoUrl;
}
