package com.example.authservice.exceptions;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("User credentials not sufficient");
    }
}
