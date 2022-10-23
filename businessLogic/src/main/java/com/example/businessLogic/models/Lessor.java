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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lessor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lessor_id")
    private long id;

    @Column(name = "lessor_name", nullable = false)
    @NotNull(message = "name is required")
    @Pattern(regexp = "([a-zA-Z]+ )*[a-zA-Z]+", message = "name should consist of words separated by a space")
    private String name;

    @Column(name = "lessor_email", nullable = false)
    @NotNull(message = "email is required")
    @Pattern(regexp = "(\\w+[.-]?)*\\w+@(\\w{2,4}\\.)+\\w{2,4}", message = "email format is incorrect")
    private String email;

    @Column(name = "lessor_phone", nullable = false)
    @NotNull(message = "phone is required")
    @Pattern(regexp = "(\\+\\d{12})|(\\d{10})", message = "phone format is incorrect")
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
