package com.example.businessLogic.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lessor {

    // TODO: add validation for fields
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lessor_id")
    private long id;

    @Column(name = "lessor_name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "lessor_email", nullable = false)
    private String email;

    @Column(name = "lessor_phone", nullable = false)
    private String phone;

    @OneToMany(mappedBy = "lessor")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Set<Room> rooms = new HashSet<>();

    public Lessor(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Lessor(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
