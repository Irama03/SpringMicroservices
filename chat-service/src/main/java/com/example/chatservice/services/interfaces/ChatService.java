package com.example.chatservice.services.interfaces;

import com.example.chatservice.dtos.chats.ChatPostDto;
import com.example.chatservice.models.Chat;

public interface ChatService {

    Chat add(ChatPostDto chatPostDto);

    Chat getById(Long id);

    void deleteById(Long id);

    Iterable<Chat> getAll();

}
