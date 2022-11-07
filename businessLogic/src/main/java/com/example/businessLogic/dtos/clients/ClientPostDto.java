package com.example.businessLogic.dtos.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ClientPostDto {

    @JsonProperty("name")
    @NotNull(message = "name is required")
    @Pattern(regexp = "([a-zA-Z]+ )*[a-zA-Z]+", message = "name should consist of one word or multiple words separated by a space")
    private String name;

    @JsonProperty("email")
    @NotNull(message = "email is required")
    @Pattern(regexp = "(\\w+\\.?)*\\w+@(\\w{2,5}\\.)+\\w{2,5}", message = "email format is incorrect")
    private String email;

    @JsonProperty("phone")
    @NotNull(message = "phone is required")
    @Pattern(regexp = "(\\+\\d{12})|(\\d{10})", message = "phone format is incorrect")
    private String phone;

}
