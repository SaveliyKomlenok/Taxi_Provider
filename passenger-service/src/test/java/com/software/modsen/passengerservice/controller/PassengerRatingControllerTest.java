package com.software.modsen.passengerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.passengerservice.dto.request.PassengerRatingRequest;
import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.entity.PassengerRating;
import com.software.modsen.passengerservice.repository.PassengerRatingRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PassengerRatingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassengerRatingRepository passengerRatingRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @BeforeAll
    public static void setUpPostgresDB() {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("passenger_rating_test_db")
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
        passengerRatingRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void testGetPassengerRatingById() {
        Passenger passenger = Passenger.builder()
                .firstname("Джон")
                .surname("Уильям")
                .patronymic("Смит")
                .email("john@example.com")
                .phoneNumber("+375293456789")
                .build();
        passengerRepository.save(passenger);
        PassengerRating passengerRating = PassengerRating.builder()
                .passenger(passenger)
                .rating(4.55)
                .build();
        passengerRatingRepository.save(passengerRating);

        mockMvc.perform(get("/api/v1/passenger-ratings/{id}", passengerRating.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.passengerRating").value(4.55));
    }

    @Test
    @SneakyThrows
    public void testSavePassengerRating() {
        Passenger passenger = Passenger.builder()
                .firstname("Джон")
                .surname("Уильям")
                .patronymic("Смит")
                .email("john@example.com")
                .phoneNumber("+375293456789")
                .build();
        passengerRepository.save(passenger);

        PassengerRatingRequest request = PassengerRatingRequest.builder()
                .passengerId(1L)
                .passengerRating(4.55)
                .build();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/passenger-ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<PassengerRating> passengerRatingList = passengerRatingRepository.findAll();
        assertThat(passengerRatingList).hasSize(1);
        assertThat(passengerRatingList.get(0).getRating()).isEqualTo(4.55);
        assertThat(passengerRatingList.get(0).getPassenger().getEmail()).isEqualTo("john@example.com");
    }
}
