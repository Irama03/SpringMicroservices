package com.example.chatservice.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String BUSINESS_LOGIC_ID = "businessLogic";

    @Autowired
    private Environment environment;

    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient getWebClient(WebClient.Builder builder) {
        return builder.baseUrl("http://" + BUSINESS_LOGIC_ID + "/api/").build();
//        return WebClient.builder().baseUrl("http://" + environment.getProperty("container.name") +
//                ":" + environment.getProperty("container.port") + "/api/").build();
    }

}
