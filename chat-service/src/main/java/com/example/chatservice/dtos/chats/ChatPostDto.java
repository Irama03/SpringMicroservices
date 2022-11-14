package com.example.chatservice.dtos.chats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class ChatPostDto {

    @Size(min = 2, max = 2)
    @NotNull
    private Set<Long> users;

}
