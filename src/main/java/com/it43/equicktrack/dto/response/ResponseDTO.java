package com.it43.equicktrack.dto.response;

import java.util.Objects;

public record ResponseDTO<T> (int status, String message, T data){
    public ResponseDTO {
        Objects.nonNull(status);
        Objects.nonNull(message);
    }
}