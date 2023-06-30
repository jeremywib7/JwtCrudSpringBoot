package com.j23.server.exception.email;

public class EmailNotVerifiedException extends Exception {
    public EmailNotVerifiedException() {
        super("Email not verified");
    }
}
