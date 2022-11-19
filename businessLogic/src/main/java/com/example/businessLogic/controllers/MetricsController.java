package com.example.businessLogic.controllers;

import com.example.businessLogic.metrics.ErrorMetricsAspect;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/metrics")
public class MetricsController {

    private final ErrorMetricsAspect errorMetricsAspect;

    public MetricsController(ErrorMetricsAspect errorMetricsAspect) {
        this.errorMetricsAspect = errorMetricsAspect;
    }

    @GetMapping("/errors")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ErrorMetricsAspect getErrorMetrics() {
        return errorMetricsAspect;
    }

}
