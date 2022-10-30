package com.example.chatservice.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Autowired
    private Environment environment;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder().baseUrl("http://" + environment.getProperty("container.name") +
                ":" + environment.getProperty("container.port") + "/api/").build();
    }

}
