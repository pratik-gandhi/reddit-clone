package com.gandhi.reddit.backend.exception;

import org.springframework.mail.MailException;

public class EmailException extends RuntimeException {
    public EmailException(String message, MailException e) {
        super(message, e);
    }
}
