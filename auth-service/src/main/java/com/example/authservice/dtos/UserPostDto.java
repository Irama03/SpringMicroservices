package com.example.authservice.dtos;

import com.example.authservice.models.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserPostDto {

    @JsonProperty("name")
    @NotNull(message = "name is required")
    @Pattern(regexp = "([a-zA-Z]+ )*[a-zA-Z]+", message = "name should consist of one word or multiple words separated by a space")
    private String name;

    @JsonProperty("email")
    @NotNull(message = "email is required")
    @Pattern(regexp = "(\\w+\\.?)*\\w+@(\\w{2,5}\\.)+\\w{2,5}", message = "email format is incorrect")
    private String email;

    @JsonProperty("phone")
    @Pattern(regexp = "(\\+\\d{12})|(\\d{10})", message = "phone format is incorrect")
    private String phone;

    @JsonProperty("password")
    @NotNull(message = "password is required")
    @Size(min = 4, message = "password length is too short (must be longer than 4 characters)")
    @Size(max = 100, message = "password length is too long (must be shorter than 100 characters)")
    private String password;

    @JsonProperty("role")
    @NotNull
    private UserRole role;

}


