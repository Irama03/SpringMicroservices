package com.example.chatservice.dtos.messages;

import com.example.chatservice.dtos.chats.ChatSlimGetDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class MessagePostDto {

    @JsonProperty("senderId")
    private Long senderId;

    @JsonProperty("chat")
    @NotNull
    private ChatSlimGetDto chat;

    @JsonProperty("text")
    @NotNull
    private String text;

}
