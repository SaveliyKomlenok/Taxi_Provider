package com.software.modsen.passengerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.passengerservice.dto.request.PassengerCreateRequest;
import com.software.modsen.passengerservice.dto.request.PassengerUpdateRequest;
import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.repository.PassengerRepository;
import com.software.modsen.passengerservice.util.TestEntities;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static com.software.modsen.passengerservice.util.TestEntities.EXPECTED_LIST_SIZE;
import static com.software.modsen.passengerservice.util.TestEntities.EXPECTED_PASSENGER_LIST_SIZE;
import static com.software.modsen.passengerservice.util.TestEntities.FIRST_INDEX;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_BASE_URL;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_EMAIL;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_PHONE_NUMBER;
import static com.software.modsen.passengerservice.util.TestEntities.SECOND_PASSENGER_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PassengerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassengerRepository passengerRepository;

    private ObjectMapper objectMapper;

    @BeforeAll
    public static void setUpPostgresDB() {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("passenger_test_db")
                .withUsername("postgres")
                .withPassword("root");
        postgreSQLContainer.start();

        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        passengerRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void testGetAllPassengers() {
        Passenger firstPassenger = TestEntities.getPassengerForIT();
        Passenger secondPassenger = TestEntities.getSecondPassengerForIT();
        passengerRepository.save(firstPassenger);
        passengerRepository.save(secondPassenger);

        mockMvc.perform(get(PASSENGER_BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.passengerResponseList").isArray())
                .andExpect(jsonPath("$.passengerResponseList.length()").value(EXPECTED_PASSENGER_LIST_SIZE))
                .andExpect(jsonPath("$.passengerResponseList[0].email").value(PASSENGER_EMAIL))
                .andExpect(jsonPath("$.passengerResponseList[1].email").value(SECOND_PASSENGER_EMAIL));
    }

    @Test
    @SneakyThrows
    public void testGetPassengerById() {
        Passenger passenger = TestEntities.getPassengerForIT();
        passengerRepository.save(passenger);

        mockMvc.perform(get(PASSENGER_BASE_URL + "/{id}", passenger.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(PASSENGER_EMAIL));
    }

    @Test
    @SneakyThrows
    public void testCreatePassenger() {
        PassengerCreateRequest request = TestEntities.getPassengerCreateRequestForIT();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(PASSENGER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Passenger> passengers = passengerRepository.findAll();
        assertThat(passengers).hasSize(EXPECTED_LIST_SIZE);
        assertThat(passengers.get(FIRST_INDEX).getEmail()).isEqualTo(PASSENGER_EMAIL);
        assertThat(passengers.get(FIRST_INDEX).getPhoneNumber()).isEqualTo(PASSENGER_PHONE_NUMBER);
    }

    @Test
    @SneakyThrows
    public void testUpdatePassenger() {
        Passenger passenger = TestEntities.getPassengerForIT();

        passengerRepository.save(passenger);

        PassengerUpdateRequest request = TestEntities.getPassengerUpdateRequestForIT();
        request.setId(passenger.getId());
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(PASSENGER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Passenger updatedPassenger = passengerRepository.findById(passenger.getId()).orElseThrow();
        assertThat(updatedPassenger.getEmail()).isEqualTo(SECOND_PASSENGER_EMAIL);
    }

    @Test
    @SneakyThrows
    public void testChangeRestrictionsStatus() {
        Passenger passenger = TestEntities.getPassengerForIT();
        passengerRepository.save(passenger);

        mockMvc.perform(put(PASSENGER_BASE_URL + "/{id}", passenger.getId()))
                .andExpect(status().isOk());

        Passenger updatedPassenger = passengerRepository.findById(passenger.getId()).orElseThrow();
        assertThat(updatedPassenger.isRestricted()).isTrue();
    }
}
