package com.example.businessLogic.controllers;

import com.example.businessLogic.metrics.RequestTimeTracker;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.example.businessLogic.metrics.ErrorMetricsAspect;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
@AllArgsConstructor
public class MetricsController {

    private RequestTimeTracker timeTracker;

    private ErrorMetricsAspect errorMetricsAspect;


    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Map<String, Double> getAllMetrics() {
        return timeTracker.getMetrics();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void resetMetrics() {
        timeTracker.reset();
    }

    @GetMapping("/errors")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ErrorMetricsAspect getErrorMetrics() {
        return errorMetricsAspect;
    }



}
