package com.example.chatservice.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "chat")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Message> messages = new HashSet<>();

    @OneToMany(mappedBy = "chat")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<ChatParticipation> users = new HashSet<>();

}
