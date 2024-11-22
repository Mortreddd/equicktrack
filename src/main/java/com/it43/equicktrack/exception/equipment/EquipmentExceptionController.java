package com.it43.equicktrack.exception.equipment;

import com.it43.equicktrack.exception.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class EquipmentExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EquipmentNotAvailableException.class)
    public ResponseEntity<Object> equipmentNotAvailableException(EquipmentNotAvailableException exception, WebRequest request) {

        ErrorDetails details = ErrorDetails
                .builder()
                .details(request.getDescription(false))
                .message(exception.getMessage())
                .date(new Date())
                .build();


//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(details);
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }
}
