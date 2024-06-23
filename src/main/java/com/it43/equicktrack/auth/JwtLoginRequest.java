package com.it43.equicktrack.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtLoginRequest {
    private String email;
    private String password;
}
