package com.example.reportservice.apiCommunication;

import com.example.reportservice.models.StatisticsValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class BusinessLogicWebClient {

    @Autowired
    private WebClient client;

    public Iterable<StatisticsValues> fetchDataForStatistics() {
        return this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("data_for_statistics")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(StatisticsValues.class).toIterable(); //??? bodyToMono
                //.block();
    }

}
