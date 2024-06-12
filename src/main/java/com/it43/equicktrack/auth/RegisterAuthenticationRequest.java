package com.it43.equicktrack.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterAuthenticationRequest {
    private String firstName;
    private String lastName;

    private String email;
    private String password;
    private String roleName;
}
