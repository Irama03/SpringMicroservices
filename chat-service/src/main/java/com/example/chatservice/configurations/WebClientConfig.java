package com.example.chatservice.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String businessLogicServiceURL = "http://localhost:8080/api/";

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder().baseUrl(businessLogicServiceURL).build();
    }

}
