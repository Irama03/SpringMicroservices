package com.example.emailservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailMessage {

    private String from;
    private String to;
    private String text;
    private String subject;

}
