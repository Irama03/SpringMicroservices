package com.example.chatservice.dtos.chats;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChatSlimGetDto {

    @JsonProperty("id")
    @NotNull
    private Long id;

}
