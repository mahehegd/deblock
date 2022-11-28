package org.deblock.exercise.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.deblock.exercise.model.SearchRequestParam;
import org.deblock.exercise.model.SearchResponseParam;
import org.deblock.exercise.model.Suppliers;
import org.deblock.exercise.service.DeblockFlightsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DeblockFlightController.class)
public class DeblockFlightControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeblockFlightsService service;

    @Test
    public void testHome() throws Exception {
        String uri = "/";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Hi deblockFlights!");
    }

    @Test
    public void testGetFlights() throws Exception {
        String uri = "/v1/flights";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        List<SearchResponseParam> response = new ArrayList<>();
        SearchResponseParam responseParam = new SearchResponseParam("CrazyAir", Suppliers.crazyAir, 100.00, "LHR",
                "AMS", LocalDate.now(), LocalDate.now().plusDays(2));
        response.add(responseParam);

        Mockito.when(service.getFlights(Mockito.any())).thenReturn(response);

        SearchRequestParam request = new SearchRequestParam("LHR", "AMS", LocalDate.now(), LocalDate.now().plusDays(2),
                2);

        String inputJson = objectMapper.writeValueAsString(request);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        List<SearchResponseParam> result = objectMapper.readValue(content,
                new TypeReference<List<SearchResponseParam>>() {
                });
        assertEquals(result.size(), 1);
    }

    @Test
    public void testGetFlightsForInvalidDepartureDate() throws Exception {
        String uri = "/v1/flights";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        SearchRequestParam request = new SearchRequestParam("LHR", "AMS", LocalDate.now().minusDays(1), LocalDate.now().plusDays(2),
                2);

        String inputJson = objectMapper.writeValueAsString(request);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        Exception exception = mvcResult.getResolvedException();
        assertEquals(400, status);
        assertInstanceOf(MethodArgumentNotValidException.class, exception);
    }

    @Test
    public void testGetFlightsForInvalidOrigin() throws Exception {
        String uri = "/v1/flights";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        SearchRequestParam request = new SearchRequestParam("LH", "AMS", LocalDate.now(), LocalDate.now().plusDays(2),
                2);

        String inputJson = objectMapper.writeValueAsString(request);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String message = mvcResult.getResolvedException().getMessage();
        assertEquals(400, status);
        assertTrue(message.contains("Origin should be 3 character long"));
    }
}
