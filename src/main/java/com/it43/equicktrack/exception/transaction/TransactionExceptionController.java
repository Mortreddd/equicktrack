package com.it43.equicktrack.exception.transaction;

import com.it43.equicktrack.exception.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class TransactionExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TransactionAlreadyExistsException.class)
    public ResponseEntity<Object> transactionAlreadyExists(TransactionAlreadyExistsException exception, WebRequest request) {
        ErrorDetails details = ErrorDetails
                .builder()
                .date(new Date())
                .message(exception.getMessage())
                .details(request.getDescription(false))
                .build();

        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }
}
