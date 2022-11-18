package com.example.emailservice.services.implementations;

import com.example.emailservice.models.EmailMessage;
import com.example.emailservice.models.LogMessage;
import com.example.emailservice.services.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "jms", name = "enabled", havingValue = "true")
public class JMSReceiver {

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.username}")
    private String to;

    @JmsListener(destination = "LogsTopic", selector = "logLevel = 'INFO' OR logLevel = 'WARN'")
    public void receiveMessage(LogMessage message) {
        emailService.sendEmail(new EmailMessage(from, to, message.getText(), "App email notification: "+message.getLevel()));
    }


}
