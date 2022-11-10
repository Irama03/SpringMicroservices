package com.example.chatservice.apiCommunication;

import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JMSService {

    private JmsTemplate jmsTemplate;

    public void sendMessageToQueue() {
        jmsTemplate.convertAndSend("TestQueue", "Test message");
    }

    @JmsListener(destination = "TestQueue")
//    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(String message) {
        System.out.println("Received message = '"+message+"'");
    }

//    @Qualifier("secondJmsTemplate")
//    JmsTemplate jmsTemplate

}
