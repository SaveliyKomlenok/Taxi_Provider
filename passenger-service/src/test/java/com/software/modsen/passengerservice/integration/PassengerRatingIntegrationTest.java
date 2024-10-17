package com.software.modsen.passengerservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.passengerservice.dto.request.PassengerRatingRequest;
import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.entity.PassengerRating;
import com.software.modsen.passengerservice.repository.PassengerRatingRepository;
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

import static com.software.modsen.passengerservice.util.TestEntities.FIRST_INDEX;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_EMAIL;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_RATING;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_RATING_BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PassengerRatingIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassengerRatingRepository passengerRatingRepository;

    @Autowired
    private PassengerRepository passengerRepository;
    private ObjectMapper objectMapper;

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

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        passengerRatingRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void testGetPassengerRatingById() {
        Passenger passenger = TestEntities.getPassengerForIT();
        passengerRepository.save(passenger);

        PassengerRating passengerRating = TestEntities.getPassengerRatingForIT(passenger);
        passengerRating.setPassenger(passenger);
        passengerRatingRepository.save(passengerRating);

        mockMvc.perform(get(PASSENGER_RATING_BASE_URL + "/{id}", passengerRating.getPassenger().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.passengerRating").value(PASSENGER_RATING));
    }

    @Test
    @SneakyThrows
    public void testSavePassengerRating() {
        Passenger passenger = TestEntities.getPassengerForIT();
        passengerRepository.save(passenger);

        PassengerRatingRequest request = TestEntities.getPassengerRatingRequestForIT(passenger.getId());
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(PASSENGER_RATING_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<PassengerRating> passengerRatingList = passengerRatingRepository.findAll();
        assertThat(passengerRatingList.get(FIRST_INDEX).getRating()).isEqualTo(PASSENGER_RATING);
        assertThat(passengerRatingList.get(FIRST_INDEX).getPassenger().getEmail()).isEqualTo(PASSENGER_EMAIL);
    }
}
