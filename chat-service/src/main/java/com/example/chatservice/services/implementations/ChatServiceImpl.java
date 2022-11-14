package com.example.chatservice.services.implementations;

import com.example.chatservice.apiCommunication.BusinessLogicWebClient;
import com.example.chatservice.dtos.chats.ChatPostDto;
import com.example.chatservice.exceptions.RecordNotFoundException;
import com.example.chatservice.exceptions.ValueNotUniqueException;
import com.example.chatservice.models.Chat;
import com.example.chatservice.models.ChatParticipation;
import com.example.chatservice.repositories.ChatParticipationRepository;
import com.example.chatservice.repositories.ChatRepository;
import com.example.chatservice.services.interfaces.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatParticipationRepository participationRepository;
    private final BusinessLogicWebClient webClient;


    @Override
    public Iterable<Chat> getAll() {
        return chatRepository.findAll();
    }

    @Override
    public Chat getById(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(Chat.class, "id", id));
    }

    @Override
    public Chat addNoCheck(ChatPostDto chatPostDto) {
        Optional<Chat> existingChat = getByUserIds(chatPostDto.getUsers());
        if (existingChat.isPresent()) {
            return existingChat.get();
        } else {
            Chat createdChat = chatRepository.save(new Chat());
            for (Long userId : chatPostDto.getUsers()) {
                ChatParticipation participation = new ChatParticipation(createdChat, userId);
                participationRepository.save(participation);
                createdChat.getUsers().add(participation);
            }
            return chatRepository.save(createdChat);
        }
    }

    @Override
    public Chat add(ChatPostDto chatPostDto, String authToken) {
        chatPostDto.getUsers().forEach(id -> webClient.fetchUser(id, authToken));
        return addNoCheck(chatPostDto);
    }

    @Override
    public Optional<Chat> getByUserIds(Set<Long> userIds) {
        List<Set<Long>> usersChatIds = userIds.stream()
                .map(participationRepository::findChatParticipationsByUserId)
                .collect(Collectors.toList());
        Set<Long> commonChatIds = usersChatIds.get(0).stream()
                .filter(id -> usersChatIds.get(1).contains(id))
                .collect(Collectors.toSet());

        if (commonChatIds.size() > 1) {
            throw new ValueNotUniqueException("Chat users", userIds);
        } else if (commonChatIds.size() == 1) {
            return chatRepository.findById(commonChatIds.iterator().next());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        chatRepository.deleteById(id);
    }

}
