package com.example.chatservice.services.implementations;

import com.example.chatservice.dtos.chats.ChatPostDto;
import com.example.chatservice.models.BookingMessage;
import com.example.chatservice.models.Chat;
import com.example.chatservice.models.Message;
import com.example.chatservice.services.interfaces.ChatService;
import com.example.chatservice.services.interfaces.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.example.chatservice.config.JMSConfig.BOOKING_QUEUE;

@Service

@RequiredArgsConstructor
public class JMSReceiver {

    private final ChatService chatService;
    private final MessageService messageService;


    @JmsListener(destination = BOOKING_QUEUE, containerFactory = "queueConnectionFactory")
    public void receiveMessage(BookingMessage bookingMessage) {
        ChatPostDto chatPostDto = new ChatPostDto(
                Set.of(bookingMessage.getLessorId(), bookingMessage.getClientId()));
        Chat chat = chatService.addNoCheck(chatPostDto);
        Message message = new Message(bookingMessage.getLessorId(), chat, bookingMessage.getText());
        messageService.addMessageNoCheck(message);
    }


}
