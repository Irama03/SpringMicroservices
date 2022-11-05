package com.example.businessLogic.controllers;

import com.example.businessLogic.models.StatisticsValues;
import com.example.businessLogic.services.interfaces.DataForStatisticsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/data_for_statistics")
@Validated
public class DataForStatisticsController {

    private final DataForStatisticsService dataForStatisticsService;

    public DataForStatisticsController(DataForStatisticsService dataForStatisticsService) {
        this.dataForStatisticsService = dataForStatisticsService;
    }

    @GetMapping
    public Iterable<StatisticsValues> getDataForStatistics() {
        return dataForStatisticsService.getDataForStatistics();
    }

}
