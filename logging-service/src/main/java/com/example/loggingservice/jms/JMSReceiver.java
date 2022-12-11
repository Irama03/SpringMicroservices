package com.example.loggingservice.jms;

import com.example.loggingservice.models.LogEntity;
import com.example.loggingservice.models.LogMessageDTO;
import com.example.loggingservice.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class JMSReceiver {

    @Autowired
    private LogService logService;

    @JmsListener(destination = "LogsTopic", selector = "logLevel = 'ERROR' OR logLevel = 'WARN'")
    public void receiveMessage(LogMessageDTO dto) {
        LogEntity logEntity = new LogEntity(dto.getServiceTag(), dto.getMessage().getText(), dto.getMessage().getLevel());
        System.out.println(logEntity.getServiceTag() + ": " + logEntity.getMessage());
        logService.save(logEntity);
    }

}
