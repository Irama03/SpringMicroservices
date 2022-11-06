package com.example.reportservice.apiCommunication;

import com.example.reportservice.models.StatisticsValues;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class BusinessLogicWebClient {

    private final WebClient client;

    public BusinessLogicWebClient(WebClient client) {
        this.client = client;
    }

    public Flux<StatisticsValues> fetchDataForStatistics(String authToken) {
        return this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("data_for_statistics")
                        .build()).header(HttpHeaders.AUTHORIZATION, authToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(StatisticsValues.class);
    }

}
