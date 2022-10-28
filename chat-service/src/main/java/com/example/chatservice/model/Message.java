package com.example.chatservice.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
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
