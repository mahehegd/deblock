package org.deblock.exercise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.deblock.exercise.model.SearchRequestParam;
import org.deblock.exercise.model.SearchResponseParam;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.google.common.collect.Ordering;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("wiremock")
public class DeblockFlightServiceTest {

    private static String urlToStub;
    private static WireMockServer mockServer;

    @Autowired
    DeblockFlightsService service;

    @BeforeAll
    static void init(){
        mockServer = new WireMockServer(new WireMockConfiguration().port(8001));
        WireMock.configureFor("localhost",8001);
        mockServer.start();
    }

    @AfterAll
    static void stopServer() {
        mockServer.shutdown();
    }

    @Test
    void TestResponseAndSortByFare() throws Exception {
        urlToStub = "v1/flights";
        mockServer.stubFor(WireMock.get(urlToStub)
                .willReturn(WireMock.aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("toughjet.json")));
        SearchRequestParam searchParam = new SearchRequestParam("LHR", "AMS", LocalDate.now(), LocalDate.now(), 4);

        List<SearchResponseParam> response = service.fetchFlights(searchParam);
        assertEquals(response.size(), 3);
        List<Double> fares = response.stream()
        .map(r->{return r.getFare();})
        .collect(Collectors.toList());
        assertTrue(Ordering.natural().isOrdered(fares));
    }
}
