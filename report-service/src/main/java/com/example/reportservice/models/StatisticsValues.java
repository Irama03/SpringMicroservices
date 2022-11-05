package com.example.reportservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsValues {

    @JsonProperty("lessorId")
    @NotNull
    private Long lessorId;

    @JsonProperty("name")
    @NotNull
    private String name;

    @JsonProperty("quantityOfFinishedBookings")
    @NotNull
    @Min(0)
    private Integer quantityOfFinishedBookings;

    @JsonProperty("quantityOfBookingsInProgress")
    @NotNull
    @Min(0)
    private Integer quantityOfBookingsInProgress;

    @JsonProperty("lessorIncome")
    @NotNull
    @Min(0)
    private Double lessorIncome;

    @JsonProperty("quantityOfClients")
    @NotNull
    @Min(0)
    private Integer quantityOfClients;

}
