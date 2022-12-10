package com.example.chatservice.apiCommunication;

import com.example.chatservice.exceptions.UserNotFoundException;
import com.example.chatservice.proto.UserProto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthServiceProtoClient {

    private final WebClient client;

    public AuthServiceProtoClient(WebClient client) {
        this.client = client;
    }

    public UserProto.User fetchUser(Long userId, String authToken) {
        return this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("api/users/" + userId)
                        .build()).header(HttpHeaders.AUTHORIZATION, authToken)
                .accept(MediaType.parseMediaType("application/x-protobuf"))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> response.bodyToMono(String.class).map(UserNotFoundException::new))
                .bodyToMono(UserProto.User.class)
                .block();
    }

}
