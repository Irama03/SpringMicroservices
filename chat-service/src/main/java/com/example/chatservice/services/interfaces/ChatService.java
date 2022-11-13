package com.example.chatservice.services.interfaces;

import com.example.chatservice.dtos.chats.ChatPostDto;
import com.example.chatservice.models.Chat;

import java.util.Optional;
import java.util.Set;

public interface ChatService {

    Chat add(ChatPostDto chatPostDto, String authToken);

    Chat getById(Long id);

    void deleteById(Long id);

    Iterable<Chat> getAll();

    Optional<Chat> getByUserIds(Set<Long> userIds);

}
