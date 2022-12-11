package com.example.authservice.exceptions;

public class RecordAlreadyExistsException extends RuntimeException {
    public RecordAlreadyExistsException(String property) {
        super("User with such " + property + " already exists");
    }
}
