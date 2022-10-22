package com.example.businessLogic.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public Lessor(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
