package com.example.businessLogic.exceptions;

public class ValueNotUniqueException extends RuntimeException {

    public ValueNotUniqueException(String fieldName, Object value) {
        super("Value '"+value+"' for field '"+fieldName+"' is not unique!");
    }
}
