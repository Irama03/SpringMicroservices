package com.example.authservice.exceptions;

public class InvalidRequestBodyException extends RuntimeException {
    public InvalidRequestBodyException(String msg) {
        super(msg);
    }
}
