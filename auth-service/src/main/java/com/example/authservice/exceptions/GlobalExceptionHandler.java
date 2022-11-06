package com.example.authservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e){
        Map<String,String> map = new HashMap<>();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

}
