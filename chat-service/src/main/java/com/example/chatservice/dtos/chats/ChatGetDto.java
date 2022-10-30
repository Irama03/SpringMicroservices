package com.example.chatservice.dtos.chats;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class ChatGetDto {

    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("users")
    @NotNull
    private Set<Long> users;

}
