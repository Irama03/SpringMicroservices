package com.example.chatservice.apiCommunication;

import com.example.chatservice.dtos.clients.ClientSlimGetDto;
import com.example.chatservice.exceptions.RecordNotFoundException;
import com.example.chatservice.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class BusinessLogicWebClient {

    private final WebClient client;

    public BusinessLogicWebClient(WebClient client) {
        this.client = client;
    }

    public ClientSlimGetDto fetchUser(Long userId) {
        return this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("clients/" + userId)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> response.bodyToMono(String.class).map(UserNotFoundException::new))
                .bodyToMono(ClientSlimGetDto.class)
                .block();
    }

}
