package com.example.chatservice.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ChatParticipation {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "chatId", column = @Column(name = "chat_id")),
            @AttributeOverride(name = "userId", column = @Column(name = "user_id"))
    })
    private ChatParticipationId id;

    @ManyToOne
    @NotNull
    @MapsId(value = "chatId")
    private Chat chat;

}
