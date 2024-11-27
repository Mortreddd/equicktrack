package com.it43.equicktrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException exception, WebRequest request){
        ErrorDetails details = ErrorDetails.builder()
                .date(new Date())
                .message(exception.getMessage())
                .details(request.getDescription(false))
                .build();

//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailMessageException.class)
    public ResponseEntity<Object> emailMessageException(
            EmailMessageException invalidOtpException,
            WebRequest webRequest
    ){
        ErrorDetails details = new ErrorDetails(
                new Date(),
                webRequest.getDescription(false),
                invalidOtpException.getMessage()
        );

//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
        return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Object> fileNotFound(
            FileNotFoundException fileNotFoundException,
            WebRequest webRequest
    ){
        ErrorDetails details = new ErrorDetails(
                new Date(),
                webRequest.getDescription(false),
                fileNotFoundException.getMessage()
        );
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

}
