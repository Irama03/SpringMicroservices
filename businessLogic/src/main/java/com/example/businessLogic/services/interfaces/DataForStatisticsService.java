package com.example.businessLogic.services.interfaces;

import com.example.businessLogic.models.StatisticsValues;

public interface DataForStatisticsService {

    Iterable<StatisticsValues> getDataForStatistics();

}
