package com.example.chatservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogMessage {

    private String level;
    private String text;
    private Date date;

    @Override
    public String toString() {
        return level + ": " + text + " - " + date;
    }

}
