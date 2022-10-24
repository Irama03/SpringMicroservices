package com.example.businessLogic.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "client_id")
    private Long id;

    @Column(name = "client_name", nullable = false)
    @NotNull
    @Pattern(regexp = "([a-zA-Z]+ )*[a-zA-Z]+", message = "name should consist of words separated by a space")
    private String name;

    @Column(nullable = false, unique = true)
    @NotNull
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "email format is incorrect")
    private String email;

    @Column(name = "client_phone",nullable = false, unique = true)
    @NotNull
    @Pattern(regexp = "(\\+\\d{12})|(\\d{10})", message = "phone format is incorrect")
    private String phone;

    @OneToMany(mappedBy = "client")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Set<Booking> bookings = new HashSet<>();

    public Client(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Client(Long id) {
        this.id = id;
    }


}
