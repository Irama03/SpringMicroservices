package com.example.businessLogic.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    @NotNull
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "lessor_id")
    private Lessor lessor;

    @NotNull
    @Column(nullable = false)
    private String address;

    @NotNull
    @Column(nullable = false, length = 50)
    private String city;

    @NotNull
    @Column(nullable = false)
    private Double price;

    @Column
    private Integer capacity;

    public Room(String name, RoomType type, String description, Lessor lessor,
                String address, String city, Double price, Integer capacity) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.lessor = lessor;
        this.address = address;
        this.city = city;
        this.price = price;
        this.capacity = capacity;
    }

    public Room(Long id, String name, RoomType type, String description, Lessor lessor,
                String address, String city, Double price, Integer capacity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.lessor = lessor;
        this.address = address;
        this.city = city;
        this.price = price;
        this.capacity = capacity;
    }
}
