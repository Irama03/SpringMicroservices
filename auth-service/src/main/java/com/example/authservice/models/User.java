package com.example.authservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, name = "user_name")
    @NotNull
    @Pattern(regexp = "([a-zA-Z]+ )*[a-zA-Z]+", message = "name should consist of words separated by a space")
    private String name;

    @Column(nullable = false, unique = true, name = "user_email")
    @NotNull
    @Pattern(regexp = "(\\w+\\.?)*\\w+@(\\w{2,5}\\.)+\\w{2,5}", message = "email format is incorrect")
    private String email;

    @Column(nullable = false, name="user_password")
    @NotNull
    @Size(min = 4, message = "password length is too short (must be longer than 4 characters)")
    @Size(max = 100, message = "password length is too long (must be shorter than 100 characters)")
    private String password;

    @Column(nullable = false, name="user_role")
    @Enumerated(EnumType.STRING)
    @NotNull
    private UserRole role;

}
