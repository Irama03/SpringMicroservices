package com.example.businessLogic.dtos.rooms;

import com.example.businessLogic.models.RoomType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RoomPutDto {

    @JsonProperty("name")
    @NotNull(message = "name is required")
    private String name;

    @JsonProperty("type")
    @NotNull
    private RoomType type;

    @JsonProperty("description")
    private String description;

    @JsonProperty("address")
    @NotNull(message = "address is required")
    private String address;

    @JsonProperty("city")
    @NotNull(message = "city is required")
    private String city;

    @JsonProperty("price")
    @NotNull(message = "price per day is required")
    @Min(value = 0, message = "price should be >= 0")
    private Double price;

    @JsonProperty("capacity")
    @Min(value = 1, message = "capacity should be >= 1")
    @Max(value = 500, message = "capacity should be <= 500")
    private Integer capacity;

}
