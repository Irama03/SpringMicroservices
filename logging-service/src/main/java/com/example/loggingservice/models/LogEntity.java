package com.example.loggingservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "logs")
@Getter
@Setter
@NoArgsConstructor
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "log_id")
    private Long id;

    @Column(nullable = false, name = "service_tag")
    private String serviceTag;

    @Column(nullable = false, name = "log_message", columnDefinition = "text")
    private String message;

    @Column(nullable = false, name="log_type")
    @Enumerated(EnumType.STRING)
    private LogType type;

    public LogEntity(String serviceTag, String message, String type) {
        this.serviceTag = serviceTag;
        this.message = message;
        this.type = LogType.valueOf(type);
    }

}
