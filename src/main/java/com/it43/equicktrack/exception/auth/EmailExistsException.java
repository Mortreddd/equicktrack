package com.it43.equicktrack.exception.auth;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException(String message){
        super(message);
    }
}
