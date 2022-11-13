package com.example.loggingservice.services;

import com.example.loggingservice.models.LogEntity;
import com.example.loggingservice.models.LogType;

public interface LogService {

    LogEntity save(LogEntity log);

    Iterable<LogEntity> getByServiceTag(String tag);

    Iterable<LogEntity> getByType(LogType type);

    Iterable<LogEntity> getAll();

}
