package com.it43.equicktrack.dto.response;

import lombok.Data;

@Data
public class ResponseDTO <T> {
    private int status;
    private String message;
    private T data;
}
