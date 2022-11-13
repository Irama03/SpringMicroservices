package com.example.reportservice.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ModelAndView catchOtherExceptions(Exception e) {
        return getModelAndViewFromException(e, HttpStatus.INTERNAL_SERVER_ERROR);
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
