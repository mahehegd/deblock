package org.deblock.exercise.sao.ToughJet;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ToughJetRequestObject {
    
    private String from;

    private String to;

    private LocalDate outboundDate;

    private LocalDate inboundDate;

    private int numberOfAdults;
}
