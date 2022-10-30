package com.example.chatservice.services.implementations;

import com.example.chatservice.apiCommunication.BusinessLogicWebClient;
import com.example.chatservice.exceptions.RecordNotFoundException;
import com.example.chatservice.models.Chat;
import com.example.chatservice.models.Message;
import com.example.chatservice.repositories.ChatRepository;
import com.example.chatservice.repositories.MessageRepository;
import com.example.chatservice.services.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final BusinessLogicWebClient webClient;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, ChatRepository chatRepository, BusinessLogicWebClient webClient) {
        this.messageRepository = messageRepository;
        this.webClient = webClient;
        this.chatRepository = chatRepository;
    }

    @Override
    public Iterable<Message> getMessagesOfChat(Long chatId) {
        if (!chatRepository.existsById(chatId))
            throw new RecordNotFoundException(Chat.class, "id", chatId);
        return messageRepository.getMessagesByChatId(chatId);
    }

    @Override
    public Message getMessageById(Long id) {
        return messageRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Message.class, "id", id));
    }

    @Override
    public Message addMessage(Message message) {
        //TODO: check senderId
        webClient.fetchUser(message.getSenderId());
        Chat chat = chatRepository.findById(message.getChat().getId()).orElseThrow(
                () -> new RecordNotFoundException(Chat.class, "id", message.getChat().getId()));
        message.setChat(chat);
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(Long id) {
        messageRepository.findById(id).ifPresentOrElse(m -> messageRepository.deleteById(id),
                () -> {throw new RecordNotFoundException(Message.class, "id", id);});
    }

}
