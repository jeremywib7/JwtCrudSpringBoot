package com.j23.server.exception;

public class RecaptchaInvalidException extends RuntimeException {
    public RecaptchaInvalidException(String message) {
        super(message);
    }
}
