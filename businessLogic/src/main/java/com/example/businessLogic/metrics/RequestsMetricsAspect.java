package com.example.businessLogic.metrics;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RequestsMetricsAspect {

    private long lessorsRequestNumber;

    private long generalRequestNumber;

    private static final long START_MILLIS = System.currentTimeMillis();

    @Before("execution(* com.example.businessLogic.controllers.LessorController.*(..))")
    private void lessorRequestPerformed(JoinPoint jp) {
        lessorsRequestNumber++;
    }

    @Before("execution(* com.example.businessLogic.controllers.*.*(..))")
    private void generalRequestPerformed(JoinPoint jp) {
        generalRequestNumber++;
    }

    public int getLessorRequestsPerMinute() {
        final long diffMillis = System.currentTimeMillis() - START_MILLIS;
        final int diffMinutes = (int) (diffMillis / 60000);
        return (int)(lessorsRequestNumber / (diffMinutes == 0 ? 1 : diffMinutes));
    }

    public int getGeneralRequestsPerMinute() {
        final long diffMillis = System.currentTimeMillis() - START_MILLIS;
        final int diffMinutes = (int) (diffMillis / 60000);
        return (int)(generalRequestNumber / (diffMinutes == 0 ? 1 : diffMinutes));
    }

}
