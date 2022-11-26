package org.deblock.exercise.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestParam {
    @NonNull
    @Size(min = 3, max = 3, message = "Origin should be 3 character long")
    private String origin;

    @NonNull
    @Size(min = 3, max = 3, message = "Destination should be 3 character long")
    private String destination;

    @NonNull
    @FutureOrPresent(message = "Departure date should be greater than present")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate departureDate;

    @NonNull
    @FutureOrPresent(message = "Return date should be greater than present")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate returnDate;

    @Min(value = 1, message = "Minimum of 1 passenger per trip")
    @Max(value = 4, message = "Maximum of 4 passengers per trip")
    private int numberOfPassengers;
}
