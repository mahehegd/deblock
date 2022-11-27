package org.deblock.exercise.sao.ToughJet;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToughJetResponseObject {
    private String carrier;
    private Double basePrice;
    private Double tax;
    private Double discount;
    private String departureAirportName;
    private String arrivalAirportName;
    private LocalDate outboundDateTime;
    private LocalDate inboundDateTime;
}
