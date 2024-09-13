package com.it43.equicktrack.exception;


import jakarta.mail.MessagingException;

public class EmailMessageException extends MessagingException {
    public EmailMessageException(String message) {
        super(message);
    }

}
