package com.example.loggingservice.controllers;

import com.example.loggingservice.models.LogEntity;
import com.example.loggingservice.models.LogType;
import com.example.loggingservice.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/byService")
    public Iterable<LogEntity> getLogsByServiceTag(@RequestParam String tag) {
        return logService.getByServiceTag(tag);
    }

    @GetMapping("/byLevel")
    public Iterable<LogEntity> getLogsByLevel(@RequestParam LogType level) {
        return logService.getByType(level);
    }

    @GetMapping
    public Iterable<LogEntity> getAll() {
        return logService.getAll();
    }

}
