package com.example.chatservice.logging;

import com.example.chatservice.models.LogMessage;
import com.example.chatservice.services.implementations.JMSService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    JMSService jmsService;

    private final Logger logger = LoggerFactory.getLogger(GlobalLoggingAspect.class);

    @AfterReturning(returning = "returnObject", value = "execution(* com.example.chatservice.controllers.*.*(..))")
    public void objectAfterAdvice(JoinPoint joinPoint, Object returnObject) {
        LogMessage logMessage = createLogMessage("INFO", joinPoint, returnObject);
        logger.info(logMessage.toString());
        jmsService.sendMessageToTopic(logMessage);
    }

    @AfterReturning(returning = "returnObject", value = "execution(* com.example.chatservice.exceptions.handlers.GlobalExceptionHandler.handle*(..))")
    public void warnErrorAfterAdvice(JoinPoint joinPoint, Object returnObject) {
        LogMessage logMessage = createLogMessage("WARN", joinPoint, returnObject);
        logger.warn(logMessage.toString());
        jmsService.sendMessageToTopic(logMessage);
    }

    @AfterReturning(returning = "returnObject", value = "execution(* com.example.chatservice.exceptions.handlers.GlobalExceptionHandler.catchOtherExceptions(..))")
    public void errorAfterAdvice(JoinPoint joinPoint, Object returnObject) {
        LogMessage logMessage = createLogMessage("ERROR", joinPoint, returnObject);
        logger.error(logMessage.toString());
        jmsService.sendMessageToTopic(logMessage);
    }

    private LogMessage createLogMessage(String logLevel, JoinPoint joinPoint, Object returnObject) {
        return new LogMessage(logLevel, joinPoint.getSignature() + " invoked with arguments: " + Arrays.toString(joinPoint.getArgs()) + " returned " + returnObject, new Date());
    }

}