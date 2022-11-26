package org.deblock.exercise.controller;

import java.util.List;

import javax.validation.Valid;

import org.deblock.exercise.model.SearchRequestParam;
import org.deblock.exercise.model.SearchResponseParam;
import org.deblock.exercise.service.DeblockFlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class DeblockFlightController {

    @Autowired
    DeblockFlightsService service;
    @RequestMapping("/")
    public String home() {
        return "Hi deblockFlights!";
    }

    @PostMapping(value = "/v1/flights")
    public ResponseEntity<List<SearchResponseParam>> getFlights(
            @Valid @RequestBody SearchRequestParam searchParameters) {
                
        List<SearchResponseParam> response = service.fetchFlights(searchParameters);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}