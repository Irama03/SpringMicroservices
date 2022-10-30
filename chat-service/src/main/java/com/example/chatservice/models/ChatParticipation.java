package com.example.chatservice.models;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
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

    public ChatParticipation(Chat chat, Long userId) {
        this.id = new ChatParticipationId(chat.getId(), userId);
        this.chat = chat;
    }

    public Long getChatId() {
        return id.getChatId();
    }

    public Long getUserId() {
        return id.getUserId();
    }

}
