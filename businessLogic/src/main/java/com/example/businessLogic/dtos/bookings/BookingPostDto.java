package com.example.businessLogic.dtos.bookings;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ToString
public class BookingPostDto {

    @JsonProperty("room_id")
    @NotNull(message = "room id is required")
    private Long roomId;

    @JsonProperty("client_id")
    @NotNull(message = "client id is required")
    private Long clientId;

    @JsonProperty("price")
    @NotNull
    @Min(value = 0, message = "price should be >= 0")
    private Double price;

    @JsonProperty("start_date")
    @NotNull
    private Date startDate;

    @JsonProperty("end_date")
    @NotNull
    private Date endDate;

}
