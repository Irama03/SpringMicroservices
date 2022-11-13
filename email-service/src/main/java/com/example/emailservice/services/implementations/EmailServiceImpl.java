package com.example.emailservice.services.implementations;

import com.example.emailservice.models.EmailMessage;
import com.example.emailservice.services.interfaces.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;

    public void sendEmail(EmailMessage emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailMessage.getFrom());
        message.setTo(emailMessage.getTo());
        message.setSubject(emailMessage.getSubject());
        message.setText(emailMessage.getText());
        mailSender.send(message);
    }


}
