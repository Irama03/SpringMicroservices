package com.example.chatservice.dtos.mappers;

import com.example.chatservice.dtos.messages.MessageGetDto;
import com.example.chatservice.dtos.messages.MessagePostDto;
import com.example.chatservice.models.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    Iterable<MessageGetDto> messagesToMessagesGetDto(Iterable<Message> messages);
    MessageGetDto messageToMessageGetDto(Message message);

    Message messagePostDtoToMessage(MessagePostDto messagePostDto);

}
