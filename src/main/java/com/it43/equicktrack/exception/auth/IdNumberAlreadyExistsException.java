package com.it43.equicktrack.exception.auth;

public class IdNumberAlreadyExistsException extends RuntimeException{
    public IdNumberAlreadyExistsException(String message) {
        super(message);
    }
}
