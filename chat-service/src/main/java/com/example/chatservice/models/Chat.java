package com.example.chatservice.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy = "chat")
    private Set<Message> messages = new HashSet<>();

    @OneToMany(mappedBy = "chat")
    private Set<ChatParticipation> users = new HashSet<>();

}
