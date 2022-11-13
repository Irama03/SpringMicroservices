package com.example.reportservice.services.implementations;

import com.example.reportservice.models.LogMessage;
import lombok.AllArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
@AllArgsConstructor
public class JMSService {

    private JmsTemplate jmsTemplate;

    public void sendMessageToTopic(LogMessage logMessage) {
        jmsTemplate.convertAndSend("LogsTopic", logMessage, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("microserviceTag", "statistics");
                message.setStringProperty("logLevel", logMessage.getLevel());
                return message;
            }
        });
    }

}
