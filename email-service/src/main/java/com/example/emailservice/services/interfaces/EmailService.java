package com.example.emailservice.services.interfaces;

import com.example.emailservice.models.EmailMessage;

public interface EmailService {

    void sendEmail(EmailMessage message);

}
