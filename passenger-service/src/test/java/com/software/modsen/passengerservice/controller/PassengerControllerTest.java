package com.software.modsen.passengerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.passengerservice.dto.request.PassengerCreateRequest;
import com.software.modsen.passengerservice.dto.request.PassengerUpdateRequest;
import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.repository.PassengerRepository;
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

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        passengerRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void testGetAllPassengers() {
        Passenger passenger1 = Passenger.builder()
                .firstname("Алиса")
                .surname("Смит")
                .patronymic("Джонсон")
                .email("alice@example.com")
                .phoneNumber("+375291234567")
                .build();
        Passenger passenger2 = Passenger.builder()
                .firstname("Боб")
                .surname("Браун")
                .patronymic("Джонсон")
                .email("bob@example.com")
                .phoneNumber("+375299876543")
                .build();
        passengerRepository.save(passenger1);
        passengerRepository.save(passenger2);

        mockMvc.perform(get("/api/v1/passengers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.passengerResponseList").isArray())
                .andExpect(jsonPath("$.passengerResponseList.length()").value(2))
                .andExpect(jsonPath("$.passengerResponseList[0].email").value("alice@example.com"))
                .andExpect(jsonPath("$.passengerResponseList[1].email").value("bob@example.com"));
    }

    @Test
    @SneakyThrows
    public void testGetPassengerById() {
        Passenger passenger = Passenger.builder()
                .firstname("Джон")
                .surname("Уильям")
                .patronymic("Смит")
                .email("john@example.com")
                .phoneNumber("+375293456789")
                .build();
        passengerRepository.save(passenger);

        mockMvc.perform(get("/api/v1/passengers/{id}", passenger.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    @SneakyThrows
    public void testCreatePassenger() {
        PassengerCreateRequest request = PassengerCreateRequest.builder()
                .firstname("Джон")
                .surname("Уильям")
                .patronymic("Смит")
                .email("john@example.com")
                .phoneNumber("+375293456789")
                .build();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Passenger> passengers = passengerRepository.findAll();
        assertThat(passengers).hasSize(1);
        assertThat(passengers.get(0).getEmail()).isEqualTo("john@example.com");
        assertThat(passengers.get(0).getPhoneNumber()).isEqualTo("+375293456789");
    }

    @Test
    @SneakyThrows
    public void testUpdatePassenger() {
        Passenger passenger = Passenger.builder()
                .firstname("Джон")
                .surname("Уильям")
                .patronymic("Смит")
                .email("john@example.com")
                .phoneNumber("+375293456789")
                .build();

        passengerRepository.save(passenger);

        PassengerUpdateRequest request = PassengerUpdateRequest.builder()
                .id(passenger.getId())
                .firstname("Джейн")
                .surname("Уильям")
                .patronymic("Джонсон")
                .email("jane@example.com")
                .phoneNumber("+375293456789")
                .build();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/v1/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Passenger updatedPassenger = passengerRepository.findById(passenger.getId()).orElseThrow();
        assertThat(updatedPassenger.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    @SneakyThrows
    public void testChangeRestrictionsStatus() {
        Passenger passenger = Passenger.builder()
                .firstname("Джон")
                .surname("Уильям")
                .patronymic("Смит")
                .email("john@example.com")
                .phoneNumber("+375293456789")
                .build();
        passengerRepository.save(passenger);

        mockMvc.perform(put("/api/v1/passengers/{id}", passenger.getId()))
                .andExpect(status().isOk());

        Passenger updatedPassenger = passengerRepository.findById(passenger.getId()).orElseThrow();
        assertThat(updatedPassenger.isRestricted()).isTrue();
    }
}
