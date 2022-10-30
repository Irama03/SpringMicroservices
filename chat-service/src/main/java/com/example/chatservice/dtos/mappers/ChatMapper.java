package com.example.chatservice.dtos.mappers;

import com.example.chatservice.dtos.chats.ChatGetDto;
import com.example.chatservice.models.Chat;

public interface ChatMapper {

    ChatGetDto chatToGetDto(Chat chat);

    Iterable<ChatGetDto> chatsToChatGetDtos(Iterable<Chat> chats);
}
