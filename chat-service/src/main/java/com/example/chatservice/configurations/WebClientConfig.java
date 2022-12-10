package com.example.chatservice.configurations;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String AUTH_SERVICE_ID = "auth-service";

    @Bean
    public WebClient getWebClient(EurekaClient discoveryClient) {
        InstanceInfo instance = discoveryClient.getNextServerFromEureka(AUTH_SERVICE_ID, false);
        return WebClient.builder().baseUrl(instance.getHomePageUrl()).build();
    }

}
