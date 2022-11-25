package org.deblock.exercise.sao.CrazyAir;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CrazyAirRequestObject {
    private String origin;

    private String destination;

    private LocalDate departureDate;

    private LocalDate returnDate;

    private int passengerCount;
}




