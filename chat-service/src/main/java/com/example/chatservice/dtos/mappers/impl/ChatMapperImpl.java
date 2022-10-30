package com.example.chatservice.dtos.mappers.impl;

import com.example.chatservice.dtos.chats.ChatGetDto;
import com.example.chatservice.dtos.mappers.ChatMapper;
import com.example.chatservice.models.Chat;
import com.example.chatservice.models.ChatParticipation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChatMapperImpl implements ChatMapper {
    @Override
    public ChatGetDto chatToGetDto(Chat chat) {
        Set<Long> userIds = chat.getUsers()
                .stream()
                .map(ChatParticipation::getUserId)
                .collect(Collectors.toSet());
        return new ChatGetDto(chat.getId(), userIds);
    }

    @Override
    public Iterable<ChatGetDto> chatsToChatGetDtos(Iterable<Chat> chats) {
        ArrayList<ChatGetDto> dtos = new ArrayList<>();
        for (Chat chat : chats) {
            dtos.add(chatToGetDto(chat));
        }
        return dtos;
    }

}
