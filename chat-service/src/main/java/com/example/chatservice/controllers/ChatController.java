package com.example.chatservice.controllers;

import com.example.chatservice.dtos.chats.ChatGetDto;
import com.example.chatservice.dtos.chats.ChatPostDto;
import com.example.chatservice.dtos.mappers.ChatMapper;
import com.example.chatservice.services.interfaces.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public Iterable<ChatGetDto> getAllChats() {
        return mapper.chatsToChatGetDtos(chatService.getAll());
    }

    @GetMapping("/{id}")
    public ChatGetDto getChat(@PathVariable("id") Long id) {
        return mapper.chatToGetDto(chatService.getById(id));
    }

    @PostMapping
    public ChatGetDto addChat(@Valid @RequestBody ChatPostDto chatPostDto) {
        return mapper.chatToGetDto(chatService.add(chatPostDto));
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable("id") Long id) {
        chatService.deleteById(id);
    }


}
