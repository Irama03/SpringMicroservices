package com.example.reportservice.controllers;

import com.example.reportservice.services.interfaces.StatisticsService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping(path = "/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public byte[] getStatistics() {
        return statisticsService.getStatistics();
    }

}
