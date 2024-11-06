package com.it43.equicktrack.exception.firebase;

import com.it43.equicktrack.exception.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class FirebaseExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConvertMultipartFileException.class)
    public ResponseEntity<Object> convertMultipartFile(
            ConvertMultipartFileException exception,
            WebRequest request
    ) {
        ErrorDetails details = ErrorDetails
                .builder()
                .date(new Date())
                .details(request.getDescription(false))
                .message(exception.getMessage())
                .build();

//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(details);
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FirebaseFileUploadException.class)
    public ResponseEntity<Object> firebaseFileNotUpload(FirebaseFileUploadException exception, WebRequest request) {

        ErrorDetails details = ErrorDetails
                .builder()
                .date(new Date())
                .details(request.getDescription(false))
                .message(exception.getMessage())
                .build();


        return new ResponseEntity<>(details, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
