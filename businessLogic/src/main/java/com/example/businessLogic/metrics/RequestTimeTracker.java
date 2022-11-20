package com.example.businessLogic.metrics;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class RequestTimeTracker {

    private double timeAllRequestsSuccess;
    private int countAllRequestsSuccess;

    private double timeAllRequestsFailure;
    private int countAllRequestsFailure;


    private double timeAllSpecificRequestsSuccess;
    private int countAllSpecificRequestsSuccess;
    private double timeAllSpecificRequestsFailure;
    private int countAllSpecificRequestsFailure;

    public void reset() {
        timeAllRequestsSuccess = 0;
        countAllRequestsSuccess = 0;
        timeAllRequestsFailure = 0;
        countAllRequestsFailure = 0;

        timeAllSpecificRequestsSuccess = 0;
        countAllSpecificRequestsSuccess = 0;
        timeAllSpecificRequestsFailure = 0;
        countAllSpecificRequestsFailure = 0;
    }

    private double trackTime(long millsStart) {
        return (System.currentTimeMillis() - millsStart)/1000.;
    }

    @Around("execution(public * com.example.businessLogic.controllers.*.*(..))")
    public Object trackTimeAllRequests(ProceedingJoinPoint joinPoint) throws Throwable {
        long mills = System.currentTimeMillis();
        try {
            final Object res = joinPoint.proceed();
            double requestTime = trackTime(mills);
            timeAllRequestsSuccess += requestTime;
            countAllRequestsSuccess++;
            return res;
        } catch (Exception e) {
            double requestTime = trackTime(mills);
            timeAllRequestsFailure += requestTime;
            countAllRequestsFailure++;
            throw e;
        }
    }

    @Around("execution(public * com.example.businessLogic.controllers.LessorController.create(..))")
    public Object trackTimeSpecificRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        long mills = System.currentTimeMillis();
        try {
            final Object res = joinPoint.proceed();
            double requestTime = trackTime(mills);
            timeAllSpecificRequestsSuccess += requestTime;
            countAllSpecificRequestsSuccess++;
            return res;
        } catch (Exception e) {
            double requestTime = trackTime(mills);
            timeAllSpecificRequestsFailure += requestTime;
            countAllSpecificRequestsFailure++;
            throw e;
        }
    }

    private double getMetric(double time, int count) {
        return count == 0 ? -1 : time/count;
    }

    public double getAverageTimePerRequestSuccess() {
        return getMetric(timeAllRequestsSuccess, countAllRequestsSuccess);
    }

    public double getAverageTimePerRequestFailure() {
        return getMetric(timeAllRequestsFailure, countAllRequestsFailure);
    }

    public double getAverageTimePerSpecificRequestSuccess() {
        return getMetric(timeAllSpecificRequestsSuccess, countAllSpecificRequestsSuccess);
    }

    public double getAverageTimePerSpecificRequestFailure() {
        return getMetric(timeAllSpecificRequestsFailure, countAllSpecificRequestsFailure);
    }

    public Map<String, Double> getMetrics() {
        return Map.of("avgTimePerRequestOverAllRoutesSuccess", getAverageTimePerRequestSuccess(),
                "avgTimePerRequestOverAllRoutesFailure", getAverageTimePerRequestFailure(),
                "avgTimePerSpecificRequestSuccess", getAverageTimePerSpecificRequestSuccess(),
                "avgTimePerSpecificRequestFailure", getAverageTimePerSpecificRequestFailure());
    }

}
