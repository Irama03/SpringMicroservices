package com.example.chatservice.controllers;

import com.example.chatservice.dtos.mappers.MessageMapper;
import com.example.chatservice.dtos.messages.MessageGetDto;
import com.example.chatservice.dtos.messages.MessagePostDto;
import com.example.chatservice.services.interfaces.MessageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/messages")
@Validated
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    public MessageController(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @GetMapping("/of_chat/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','LESSOR')")
    public Iterable<MessageGetDto> getMessagesOfChat(@PathVariable("id") Long id){
        return messageMapper.messagesToMessagesGetDto(messageService.getMessagesOfChat(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','LESSOR')")
    public MessageGetDto getMessage(@PathVariable("id") Long id) {
        return messageMapper.messageToMessageGetDto(messageService.getMessageById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','LESSOR')")
    public MessageGetDto addMessage(@Valid @RequestBody MessagePostDto messagePostDto){
        return messageMapper.messageToMessageGetDto(messageService.addMessage(messageMapper.messagePostDtoToMessage(messagePostDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','LESSOR')")
    public void deleteMessage(@PathVariable("id") Long id) {
        messageService.deleteMessage(id);
    }
}
