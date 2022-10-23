package com.example.businessLogic.dtos.rooms;

import com.example.businessLogic.dtos.lessor.LessorSlimGetDto;
import com.example.businessLogic.models.RoomType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RoomGetDto {

    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("name")
    @NotNull
    private String name;

    @JsonProperty("type")
    @NotNull
    private RoomType type;

    @JsonProperty("description")
    private String description;

    @JsonProperty("lessor")
    @NotNull
    private LessorSlimGetDto lessor;

    @JsonProperty("address")
    @NotNull
    private String address;

    @JsonProperty("city")
    @NotNull
    private String city;

    @JsonProperty("price")
    @NotNull
    private Double price;

    @JsonProperty("capacity")
    private Integer capacity;

}
