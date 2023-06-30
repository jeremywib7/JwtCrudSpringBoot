package com.j23.server.exception.email;

public class EmailAlreadyVerifiedException extends Exception {
    public EmailAlreadyVerifiedException() {
        super("Email already verified");
    }
}
