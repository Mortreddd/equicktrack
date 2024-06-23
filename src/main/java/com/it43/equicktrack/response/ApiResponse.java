package com.it43.equicktrack.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private String message;
    private int statusCode;
    private Object data;
}
