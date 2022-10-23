package com.example.businessLogic.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "booking_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(nullable = false)
    @NotNull
    private Double price;

    @Column(nullable = false, name = "start_date")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date startDate;

    @Column(nullable = false, name = "end_date")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date endDate;

    @Column(nullable = false, name = "creation_date")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date creationDate;

}
