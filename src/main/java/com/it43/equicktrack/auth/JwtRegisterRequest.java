package com.it43.equicktrack.auth;

import com.it43.equicktrack.borrower.RoleName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtRegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
