package com.it43.equicktrack.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtRegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Integer roleId = 3;

}
