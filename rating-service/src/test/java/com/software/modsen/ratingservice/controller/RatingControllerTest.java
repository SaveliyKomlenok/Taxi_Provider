package com.software.modsen.ratingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.software.modsen.ratingservice.dto.request.RatingPassengerRequest;
import com.software.modsen.ratingservice.entity.Rating;
import com.software.modsen.ratingservice.repository.RatingRepository;
import com.software.modsen.ratingservice.util.RatingTestEntities;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.software.modsen.ratingservice.util.RatingTestEntities.DRIVER_RATING;
import static com.software.modsen.ratingservice.util.RatingTestEntities.EXPECTED_RATING_LIST_SIZE;
import static com.software.modsen.ratingservice.util.RatingTestEntities.PASSENGER_RATING;
import static com.software.modsen.ratingservice.util.RatingTestEntities.RATING_BASE_URL;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class RatingControllerTest {
    static ClientAndServer mockServerClient;

    @Autowired
    private RatingRepository ratingRepository;

    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void beforeAll() {
        mockServerClient = startClientAndServer(1111);

        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("rating_test_db")
                .withUsername("postgres")
                .withPassword("root");
        postgreSQLContainer.start();

        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("service.passenger.rating.url", () -> "http://localhost:1111/api/v1/passenger-ratings");
    }

    @BeforeEach
    public void setUp() {
        mockServerClient.reset();
        objectMapper = new ObjectMapper();
        ratingRepository.deleteAll();
    }

    @AfterAll
    static void afterAll(){
        mockServerClient.stop();
    }

    @Test
    @SneakyThrows
    public void testGetAllRatings() {
        Rating firstRating = RatingTestEntities.getTestRatingForIT();
        Rating secondRating = RatingTestEntities.getSecondTestRatingForIT();
        ratingRepository.save(firstRating);
        ratingRepository.save(secondRating);

        mockMvc.perform(get(RATING_BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ratingResponseList").isArray())
                .andExpect(jsonPath("$.ratingResponseList.length()").value(EXPECTED_RATING_LIST_SIZE))
                .andExpect(jsonPath("$.ratingResponseList[0].passengerRating").value(PASSENGER_RATING))
                .andExpect(jsonPath("$.ratingResponseList[1].driverRating").value(DRIVER_RATING));
    }

    @Test
    @SneakyThrows
    public void testRatedPassenger() {
        RatingPassengerRequest ratingPassengerRequest = RatingTestEntities.getRatingPassengerRequestForIT();
        String jsonRequest = objectMapper.writeValueAsString(ratingPassengerRequest);

        mockMvc.perform(post(RATING_BASE_URL + "/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        PassengerRatingRequest passengerRatingRequest = RatingTestEntities.getPassengerRatingRequestForIT();
        String jsonPassengerRatingRequest = objectMapper.writeValueAsString(passengerRatingRequest);

        mockServerClient.when(request().withMethod("POST"))
                .respond(response().withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonPassengerRatingRequest));

        mockServerClient.verify(request().withMethod("POST"),
                VerificationTimes.exactly(1));
    }
}
