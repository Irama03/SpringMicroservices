package com.example.chatservice.services.implementations;

import com.example.chatservice.models.BookingMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

public class BookingMessageConverter implements MessageConverter {

    @Override
    public Message toMessage(Object object, Session session) throws MessageConversionException {
        return null;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        TextMessage textMessage = (TextMessage) message;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(textMessage.getText(), BookingMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
