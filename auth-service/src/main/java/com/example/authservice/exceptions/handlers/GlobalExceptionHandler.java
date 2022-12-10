package com.example.authservice.exceptions.handlers;

import com.example.authservice.exceptions.BadCredentialsException;
import com.example.authservice.exceptions.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<Map<String,String>> handleBadCredentialsException(BadCredentialsException e) {
        return makeExceptionResponseEntity(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {RecordNotFoundException.class})
    public ResponseEntity<Map<String,String>> catchNotFoundExceptions(RecordNotFoundException e) {
        return makeExceptionResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Map<String,String>> catchOtherExceptions(Exception e) {
        return makeExceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String,String>> makeExceptionResponseEntity(Exception e, HttpStatus httpStatus) {
        return new ResponseEntity<>(makeSimpleExceptionResponse(e), httpStatus);
    }

    private Map<String, String> makeSimpleExceptionResponse(Exception e) {
        Map<String,String> map = new HashMap<>();
        map.put("success","false");
        map.put("error", e.getMessage());
        return map;
    }

    private ModelAndView wrapResponseByModelAndView(Map<String, String> response, HttpStatus status) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("errorPage");
        mav.addObject("error", response);
        mav.setStatus(status);
        return mav;
    }

    private ModelAndView getModelAndViewFromException(Exception e, HttpStatus status) {
        Map<String, String> response = makeSimpleExceptionResponse(e);
        return wrapResponseByModelAndView(response, status);
    }
}
