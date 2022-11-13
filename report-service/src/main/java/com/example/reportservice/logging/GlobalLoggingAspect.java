package com.example.reportservice.logging;

import com.example.reportservice.models.LogMessage;
import com.example.reportservice.services.implementations.JMSService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
@Aspect
public class GlobalLoggingAspect {

    @Autowired
    JMSService jmsService;

    @AfterReturning(returning = "returnObject", value = "execution(* com.example.reportservice.controllers.*.*(..))")
    public void objectAfterAdvice(JoinPoint joinPoint, Object returnObject) {
        jmsService.sendMessageToTopic(createLogMessage("INFO", joinPoint, returnObject));
    }

    @AfterReturning(returning = "returnObject", value = "execution(* com.example.reportservice.exceptions.handlers.GlobalExceptionHandler.handle*(..))")
    public void warnErrorAfterAdvice(JoinPoint joinPoint, Object returnObject) {
        jmsService.sendMessageToTopic(createLogMessage("WARN", joinPoint, returnObject));
    }

    @AfterReturning(returning = "returnObject", value = "execution(* com.example.reportservice.exceptions.handlers.GlobalExceptionHandler.catchOtherExceptions(..))")
    public void errorAfterAdvice(JoinPoint joinPoint, Object returnObject) {
        jmsService.sendMessageToTopic(createLogMessage("ERROR", joinPoint, returnObject));
    }

    private LogMessage createLogMessage(String logLevel, JoinPoint joinPoint, Object returnObject) {
        return new LogMessage(logLevel, joinPoint.getSignature() + " invoked with arguments: " + Arrays.toString(joinPoint.getArgs()) + " returned " + returnObject, new Date());
    }

}