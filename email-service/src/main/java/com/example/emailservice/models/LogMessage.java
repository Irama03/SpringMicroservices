package com.example.emailservice.models;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LogMessage {

    private String level;
    private String text;
    private Date date;

}
