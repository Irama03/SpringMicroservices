package com.example.chatservice.controllers;

import com.example.chatservice.metrics.RequestTimeTracker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
@AllArgsConstructor
public class MetricsController {

    private RequestTimeTracker timeTracker;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Map<String, Double> getAllMetrics() {
        return timeTracker.getMetrics();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void resetMetrics() {
        timeTracker.resetMetrics();
    }


}
