package com.example.chatservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingMessage {

    private String text;
    private Long lessorId;
    private Long clientId;

    @Override
    public String toString() {
        return "text='" + text + ", lessorId=" + lessorId + ", clientId=" + clientId;
    }
}
