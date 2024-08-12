package com.it43.equicktrack.exception;

import com.google.zxing.WriterException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(ConvertMultipartFileException.class)
    public ResponseEntity<?> multipartFileNotConvertedException(
            ConvertMultipartFileException convertMultipartFileException,
            WebRequest webRequest
    ){
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                convertMultipartFileException.getMessage(),
                webRequest.getDescription(false)
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorDetails);

    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> invalidTokenException(
            InvalidTokenException invalidTokenException,
            WebRequest webRequest
    ){
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                invalidTokenException.getMessage(),
                webRequest.getDescription(false)
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<?> invalidTokenException(
            EmailExistsException invalidTokenException,
            WebRequest webRequest
    ){
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                invalidTokenException.getMessage(),
                webRequest.getDescription(false)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(FirebaseFileUploadException.class)
    public ResponseEntity<?> invalidTokenException(
            FirebaseFileUploadException invalidTokenException,
            WebRequest webRequest
    ){
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                invalidTokenException.getMessage(),
                webRequest.getDescription(false)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(WriterException.class)
    public ResponseEntity<?> invalidTokenException(
            WriterException writerException,
            WebRequest webRequest
    ){
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                writerException.getMessage(),
                webRequest.getDescription(false)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> invalidTokenException(
            ExpiredJwtException expiredJwtException,
            WebRequest webRequest
    ){
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                expiredJwtException.getMessage(),
                webRequest.getDescription(false)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
    @Data
    @AllArgsConstructor
    private static class ErrorDetails{
        private Date date;
        private String message;
        private String details;
    }
}
