package com.example.businessLogic.dtos.bookings;

import com.example.businessLogic.dtos.clients.ClientSlimGetDto;
import com.example.businessLogic.dtos.rooms.RoomSlimGetDto;
import com.example.businessLogic.models.BookingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class BookingGetDto {

    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("room")
    @NotNull
    private RoomSlimGetDto room;

    @JsonProperty("client")
    @NotNull
    private ClientSlimGetDto client;

    @JsonProperty("price")
    @NotNull
    private Double price;

    @JsonProperty("start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    private Date startDate;

    @JsonProperty("end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    private Date endDate;

    @JsonProperty("creation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull
    private Date creationDate;

    @JsonProperty("status")
    @NotNull
    private BookingStatus status;
}
