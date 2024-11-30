package com.it43.equicktrack.exception.auth;

import com.it43.equicktrack.exception.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class AuthExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<Object> emailAlreadyExists(EmailExistsException exception, WebRequest request) {
        ErrorDetails details = ErrorDetails.builder()
                .details(request.getDescription(false))
                .message(exception.getMessage())
                .date(new Date())
                .build();

//        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
//                .body(errorDetails);

        return new ResponseEntity<>(details, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(InvalidOtpException.class)
    protected ResponseEntity<Object> invalidOtp(InvalidOtpException exception, WebRequest request) {
        ErrorDetails details = ErrorDetails.builder()
                .details(request.getDescription(false))
                .message(exception.getMessage())
                .date(new Date())
                .build();

//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(errorDetails);

        return new ResponseEntity<>(details, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<Object> emailNotVerified(EmailNotVerifiedException exception, WebRequest request) {
        ErrorDetails details = ErrorDetails.builder()
                .details(request.getDescription(false))
                .message(exception.getMessage())
                .date(new Date())
                .build();

//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(errorDetails);
        return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IdNumberAlreadyExistsException.class)
    public ResponseEntity<Object> emailNotVerified(IdNumberAlreadyExistsException exception, WebRequest request) {
        ErrorDetails details = ErrorDetails.builder()
                .details(request.getDescription(false))
                .message(exception.getMessage())
                .date(new Date())
                .build();

//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(errorDetails);
        return new ResponseEntity<>(details, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
