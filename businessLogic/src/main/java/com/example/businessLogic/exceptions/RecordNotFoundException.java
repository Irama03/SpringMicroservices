package com.example.businessLogic.exceptions;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(Class<?> entityClass, String fieldName, Object value) {
        super("Record of entity type '"+entityClass.getSimpleName()+
                "' not found by '"+fieldName+"' = "+value);
    }
}
