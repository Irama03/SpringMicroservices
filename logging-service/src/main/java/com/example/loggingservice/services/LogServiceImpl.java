package com.example.loggingservice.services;

import com.example.loggingservice.models.LogEntity;
import com.example.loggingservice.models.LogType;
import com.example.loggingservice.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository repo;

    @Override
    public LogEntity save(LogEntity log) {
        return repo.save(log);
    }

    @Override
    public Iterable<LogEntity> getByServiceTag(String tag) {
        return repo.findByServiceTag(tag);
    }

    @Override
    public Iterable<LogEntity> getByType(LogType type) {
        return repo.findByType(type.toString());
    }

    @Override
    public Iterable<LogEntity> getAll() {
        return repo.findAll();
    }
}
