package com.example.authservice.exceptions.handlers;

import com.example.authservice.exceptions.BadCredentialsException;
import com.example.authservice.exceptions.InvalidRequestBodyException;
import com.example.authservice.exceptions.RecordAlreadyExistsException;
import com.example.authservice.exceptions.RecordNotFoundException;
import com.example.authservice.proto.UserProto;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(value = {InvalidRequestBodyException.class, MethodArgumentNotValidException.class, InvalidFormatException.class})
    public ResponseEntity<Map<String,String>> handleInvalidRequestBodyException(Exception e) {
        return makeExceptionResponseEntity(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RecordAlreadyExistsException.class})
    public ResponseEntity<Map<String,String>> catchRecordAlreadyExistsException(RecordAlreadyExistsException e) {
        return makeExceptionResponseEntity(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {RecordNotFoundException.class})
    public ResponseEntity<UserProto.Error> catchNotFoundExceptions(RecordNotFoundException e) {
        return makeProtoError(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<UserProto.Error> catchAccessDeniedExceptions(AccessDeniedException e) {
        return makeProtoError(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Map<String,String>> catchOtherExceptions(Exception e) {
        return makeExceptionResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String,String>> makeExceptionResponseEntity(Exception e, HttpStatus httpStatus) {
        return new ResponseEntity<>(makeSimpleExceptionResponse(e), httpStatus);
    }

    private ResponseEntity<UserProto.Error> makeProtoError(Exception e, HttpStatus httpStatus) {
        return new ResponseEntity<>(UserProto.Error.newBuilder().setError(e.getMessage()).setSuccess(false).build(), httpStatus);
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
