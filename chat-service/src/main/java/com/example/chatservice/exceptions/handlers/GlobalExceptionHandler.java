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
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ModelAndView handleInvalidObjectsExceptions(MethodArgumentNotValidException e){
        Map<String,String> map = new HashMap<>();
        map.put("success","false");
        map.put("error",e.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", ")));
        return wrapResponseByModelAndView(map,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ModelAndView handleInvalidParamsExceptions(ConstraintViolationException e){
        Map<String,String> map = new HashMap<>();
        map.put("success","false");
        StringBuilder s = new StringBuilder();
        for(ConstraintViolation<?> violation: e.getConstraintViolations()){
            s.append(violation.getMessage()).append('\n');
        }
        map.put("error",s.toString());
        return wrapResponseByModelAndView(map,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RecordNotFoundException.class, UserNotFoundException.class})
    public ModelAndView handleNotFoundExceptions(Exception e){
        return getModelAndViewFromException(e,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ValueNotUniqueException.class})
    public ModelAndView handleBadRequestExceptions(Exception e){
        return getModelAndViewFromException(e,HttpStatus.BAD_REQUEST);
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
