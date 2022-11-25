package org.deblock.exercise.sao.CrazyAir;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NonNull;

@Data
public class CrazyAirResponseObject {
 
    @NonNull
    private String airline;
    private Double price;
    private CrazyAirCabinClass cabinClass;
    private String departureAirportCode;
    private String destinationAirportCode;

    @FutureOrPresent(message = "Return date should be greater than present")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departureDate;

    @FutureOrPresent(message = "Return date should be greater than present")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate arrivalDate;



}
