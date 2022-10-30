package com.example.chatservice.services.implementations;

import com.example.chatservice.dtos.chats.ChatPostDto;
import com.example.chatservice.exceptions.RecordNotFoundException;
import com.example.chatservice.models.Chat;
import com.example.chatservice.models.ChatParticipation;
import com.example.chatservice.repositories.ChatParticipationRepository;
import com.example.chatservice.repositories.ChatRepository;
import com.example.chatservice.services.interfaces.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    private final ChatParticipationRepository participationRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository, ChatParticipationRepository participationRepository) {
        this.chatRepository = chatRepository;
        this.participationRepository = participationRepository;
    }

    @Override
    public Iterable<Chat> getAll() {
        return chatRepository.findAll();
    }

    @Override
    public Chat getById(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Chat.class, "id", id));
    }

    @Override
    public Chat add(ChatPostDto chatPostDto) {
        Chat createdChat = chatRepository.save(new Chat());
        for (Long userId : chatPostDto.getUsers()) {
            ChatParticipation participation = new ChatParticipation(createdChat, userId);
            createdChat.getUsers().add(participation);
            participationRepository.save(participation);
        }
        return chatRepository.save(createdChat);
    }

    @Override
    public void deleteById(Long id) {
        chatRepository.deleteById(id);
    }

}
