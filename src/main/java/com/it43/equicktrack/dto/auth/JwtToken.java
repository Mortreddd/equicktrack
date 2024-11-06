package com.it43.equicktrack.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtToken {

    private String iss;
    private long exp;
    private long iat;
    private String accessToken;
}
