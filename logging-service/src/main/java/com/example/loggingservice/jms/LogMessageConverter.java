package com.example.loggingservice.jms;

import com.example.loggingservice.models.LogMessage;
import com.example.loggingservice.models.LogMessageDTO;
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
        String tag = message.getStringProperty("microserviceTag");
        TextMessage textMessage = (TextMessage) message;
        ObjectMapper mapper = new ObjectMapper();
        try {
            LogMessage msg = mapper.readValue(textMessage.getText(), LogMessage.class);
            return new LogMessageDTO(tag, msg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
