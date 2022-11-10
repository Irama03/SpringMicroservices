package com.example.chatservice.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "message_id")
    private long id;

    @Column(name = "sender_id", nullable = false)
    @NotNull
    private long senderId;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @NotNull
    private Chat chat;

    @Column(name = "msg_text", length = 10000, nullable = false)
    @NotNull
    private String text;

}

// p2p: when booking is created, send message from client to lessor (create chat for them if it does not exist) using queue  - Illia K.
// send logs from services (text, level, microserviceTag, date time) to topic - Ira
// microservice to filter messages and send email to email@gmail.com using topic - Illia S.
// microservice to receive logs from topic for logging (save logs to db)

