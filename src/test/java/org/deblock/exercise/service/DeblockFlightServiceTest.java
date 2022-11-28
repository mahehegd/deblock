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
    private static WireMockServer mockServer1;
    private static WireMockServer mockServer2;

    @Autowired
    DeblockFlightsService service;

    @BeforeAll
    static void init() {
        mockServer1 = new WireMockServer(new WireMockConfiguration().port(5001));
        WireMock.configureFor("localhost", 5001);
        mockServer1.start();

        mockServer2 = new WireMockServer(new WireMockConfiguration().port(5002));
        WireMock.configureFor("localhost", 5002);
        mockServer2.start();
    }

    @AfterAll
    static void stopServer() {
        mockServer1.shutdown();
        mockServer2.shutdown();
    }

    @Test
    void TestResponseAndSortByFare() throws Exception {
        String crazyAirUrlToStub = "/v1/seekflights";
        String toughJetUrlToStub = "/v1/getflights";
        mockServer1.stubFor(WireMock.get(crazyAirUrlToStub)
                .willReturn(WireMock.aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("crazyAirResponseBody.json")));
        mockServer2.stubFor(WireMock.get(toughJetUrlToStub)
                .willReturn(WireMock.aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("toughJetResponseBody.json")));
        SearchRequestParam searchParam = new SearchRequestParam("LHR", "AMS", LocalDate.now(), LocalDate.now(), 3);

        List<SearchResponseParam> response = service.getFlights(searchParam);
        assertEquals(response.size(), 5);
        List<Double> fares = response.stream()
                .map(r -> {
                    return r.getFare();
                })
                .collect(Collectors.toList());
        assertTrue(Ordering.natural().isOrdered(fares));
    }
}