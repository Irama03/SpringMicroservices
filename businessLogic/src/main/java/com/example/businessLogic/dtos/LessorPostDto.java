package com.example.businessLogic.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LessorPostDto {

    @JsonProperty("name")
    @NotNull
    private String name;

    @JsonProperty("email")
    @NotNull
    private String email;

    @JsonProperty("phone")
    @NotNull
    private String phone;

}
