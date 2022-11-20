package com.example.chatservice.metrics;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class RequestTimeTracker {

    private double webClientOverallTime;
    private int webClientProceeds;

    public void resetMetrics() {
        webClientOverallTime = 0;
        webClientProceeds = 0;
    }

    private double trackTime(long millsStart) {
        return (System.currentTimeMillis() - millsStart)/1000.;
    }

    @Around("execution(public * com.example.chatservice.apiCommunication.BusinessLogicWebClient.fetchUser(..))")
    public Object trackWebClientTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long current = System.currentTimeMillis();
        final Object res = joinPoint.proceed();
        double requestTime = trackTime(current);
        webClientOverallTime += requestTime;
        webClientProceeds++;
        return res;
    }

    private double getMetric(double time, int count) {
        return count == 0 ? -1 : time/count;
    }

    public double getAverageWebClientRequestTime() {
        return getMetric(webClientOverallTime, webClientProceeds);
    }

    public Map<String, Double> getMetrics() {
        return Map.of("avgWebClientSuccessfulRequestTime", getAverageWebClientRequestTime());
    }

}
