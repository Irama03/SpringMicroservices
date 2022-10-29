package com.example.chatservice.models;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ChatParticipationId implements Serializable {

    private long chatId;

    private long userId;

}
