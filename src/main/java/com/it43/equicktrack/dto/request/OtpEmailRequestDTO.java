package com.it43.equicktrack.dto.request;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OtpEmailRequestDTO {
    private String code;
}
