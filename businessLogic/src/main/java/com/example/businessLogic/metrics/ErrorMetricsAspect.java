package com.example.businessLogic.metrics;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Getter
public class ErrorMetricsAspect {

    @JsonProperty("quantityOfUserErrors")
    private int quantityOfUserErrors = 0;

    @JsonProperty("quantityOfServerErrors")
    private int quantityOfServerErrors = 0;

    @AfterReturning(returning = "returnObject", value =
            "execution(* com.example.businessLogic.exceptions.handlers.GlobalExceptionHandler.handle*(..))")
    public void increaseQuantityOfUserErrorsAfterAdvice(JoinPoint joinPoint, Object returnObject) {
        quantityOfUserErrors++;
    }

    @AfterReturning(returning = "returnObject", value =
            "execution(* com.example.businessLogic.exceptions.handlers.GlobalExceptionHandler.catchOtherExceptions(..))")
    public void increaseQuantityOfServerErrorsAfterAdvice(JoinPoint joinPoint, Object returnObject) {
        quantityOfServerErrors++;
    }

}
