package com.example.emailservice.services.implementations;

import com.example.emailservice.models.LogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
public class LogMessageConverter implements MessageConverter {

    @Override
    public Message toMessage(Object object, Session session) throws MessageConversionException {
        return null;
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        TextMessage textMessage = (TextMessage) message;
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(textMessage.getText(), LogMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
