package com.example.businessLogic.dtos.bookings;

import com.example.businessLogic.models.BookingStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class BookingPutDto {

    @JsonProperty("status")
    @NotNull(message = "status field is required")
    private BookingStatus status;

}
