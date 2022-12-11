package com.example.chatservice.controllers;

import com.example.chatservice.dtos.chats.ChatGetDto;
import com.example.chatservice.dtos.chats.ChatPostDto;
import com.example.chatservice.dtos.mappers.ChatMapper;
import com.example.chatservice.exceptions.RecordNotFoundException;
import com.example.chatservice.models.Chat;
import com.example.chatservice.services.interfaces.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/chats")
@Validated
public class ChatController {

    private final ChatService chatService;
    private final ChatMapper mapper;

    @Autowired
    public ChatController(ChatService chatService, ChatMapper mapper) {
        this.chatService = chatService;
        this.mapper = mapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Iterable<ChatGetDto> getAllChats() {
        return mapper.chatsToChatGetDtos(chatService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','LESSOR')")
    public ChatGetDto getChat(@PathVariable("id") Long id) {
        return mapper.chatToGetDto(chatService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','LESSOR')")
    public ChatGetDto addChat(@Valid @RequestBody ChatPostDto chatPostDto,  @RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        return mapper.chatToGetDto(chatService.add(chatPostDto, authToken));
    }

    @GetMapping("/with-users/{userIds}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','LESSOR')")
    public ChatGetDto getChatByUserIds(@PathVariable("userIds") Set<Long> userIds) {
        Optional<Chat> fetchedChat = chatService.getByUserIds(userIds);
        return mapper.chatToGetDto(fetchedChat
                .orElseThrow(() -> new RecordNotFoundException(Chat.class, "user ids", userIds)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','LESSOR')")
    public void deleteChat(@PathVariable("id") Long id) {
        chatService.deleteById(id);
    }


}
