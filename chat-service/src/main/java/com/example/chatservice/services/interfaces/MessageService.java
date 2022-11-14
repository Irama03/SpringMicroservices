package com.example.chatservice.services.interfaces;

import com.example.chatservice.models.Message;

public interface MessageService {
    Iterable<Message> getMessagesOfChat(Long chatId);
    Message getMessageById(Long id);

    Message addMessage(Message message, String authToken);
    Message addMessageNoCheck(Message message);

    void deleteMessage(Long id);

}
