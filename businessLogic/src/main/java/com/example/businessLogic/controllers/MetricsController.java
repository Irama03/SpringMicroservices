package com.example.businessLogic.controllers;

import com.example.businessLogic.metrics.RequestTimeTracker;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.example.businessLogic.metrics.ErrorMetricsAspect;
import com.example.businessLogic.metrics.RequestsMetricsAspect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
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

    private RequestsMetricsAspect requestsMetricsAspect;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Map<String, Double> getAllMetrics() {
        return timeTracker.getMetrics();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void resetMetrics() {
        timeTracker.reset();


    @GetMapping("/errors")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ErrorMetricsAspect getErrorMetrics() {
        return errorMetricsAspect;
    }

    @GetMapping("/lessorRequestsPerMinute")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String getLessorControllerMetrics() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("lessor_controller_request_per_minute", lessorControllerMetricsAspect.getLessorRequestsPerMinute());
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }

    @GetMapping("/businessLogicRequestsPerMinute")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String getGeneralControllerMetrics() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("business_logic_request_per_minute", lessorControllerMetricsAspect.getGeneralRequestsPerMinute());
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }

}
