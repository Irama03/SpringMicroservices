package com.example.chatservice.exceptions.handlers;

import com.example.chatservice.exceptions.RecordNotFoundException;
import com.example.chatservice.exceptions.UserNotFoundException;
import com.example.chatservice.exceptions.ValueNotUniqueException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String,String>> handleInvalidObjectsExceptions(MethodArgumentNotValidException e){
        Map<String,String> map = new HashMap<>();
        map.put("success","false");
        map.put("error",e.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", ")));
        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Map<String,String>> handleInvalidParamsExceptions(ConstraintViolationException e){
        Map<String,String> map = new HashMap<>();
        map.put("success","false");
        StringBuilder s = new StringBuilder();
        for(ConstraintViolation<?> violation: e.getConstraintViolations()){
            s.append(violation.getMessage()).append('\n');
        }
        map.put("error",s.toString());
        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RecordNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<Map<String,String>> handleNotFoundExceptions(Exception e){
        return new ResponseEntity<>(makeSimpleExceptionResponse(e),HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(value = {ValueNotUniqueException.class})
    public ResponseEntity<Map<String,String>> handleBadRequestException(Exception e){
        return new ResponseEntity<>(makeSimpleExceptionResponse(e),HttpStatus.BAD_REQUEST);
    }

    private Map<String, String> makeSimpleExceptionResponse(Exception e) {
        Map<String,String> map = new HashMap<>();
        map.put("success","false");
        map.put("error", e.getMessage());
        return map;
    }



}
