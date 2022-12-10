package com.example.authservice.logging;

import com.example.authservice.models.LogMessage;
//import com.example.authservice.services.JMSService;
//import org.springframework.beans.factory.annotation.Autowired;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
@Aspect
public class GlobalLoggingAspect {

    //@Autowired
    //JMSService jmsService;
    private final Logger logger = LoggerFactory.getLogger(GlobalLoggingAspect.class);

    @AfterReturning(returning = "returnObject", value = "execution(* com.example.authservice.controllers.*.*(..))")
    public void objectAfterAdvice(JoinPoint joinPoint, Object returnObject) {
        logger.info(createLogMessage("INFO", joinPoint, returnObject).toString());
        //jmsService.sendMessageToTopic(createLogMessage("INFO", joinPoint, returnObject));
    }

    @AfterReturning(returning = "returnObject", value = "execution(* com.example.authservice.exceptions.handlers.GlobalExceptionHandler.handle*(..))")
    public void warnErrorAfterAdvice(JoinPoint joinPoint, Object returnObject) {
        logger.warn(createLogMessage("WARN", joinPoint, returnObject).toString());
        //jmsService.sendMessageToTopic(createLogMessage("WARN", joinPoint, returnObject));
    }

    @AfterReturning(returning = "returnObject", value = "execution(* com.example.authservice.exceptions.handlers.GlobalExceptionHandler.catchOtherExceptions(..))")
    public void errorAfterAdvice(JoinPoint joinPoint, Object returnObject) {
        logger.error(createLogMessage("ERROR", joinPoint, returnObject).toString());
        //jmsService.sendMessageToTopic(createLogMessage("ERROR", joinPoint, returnObject));
    }

    private LogMessage createLogMessage(String logLevel, JoinPoint joinPoint, Object returnObject) {
        return new LogMessage(logLevel, joinPoint.getSignature() + " invoked with arguments: " + Arrays.toString(joinPoint.getArgs()) + " returned " + returnObject, new Date());
    }

}