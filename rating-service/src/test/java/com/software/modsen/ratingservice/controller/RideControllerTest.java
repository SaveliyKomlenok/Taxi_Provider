package com.software.modsen.ratingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.software.modsen.ratingservice.entity.Rating;
import com.software.modsen.ratingservice.repository.RatingRepository;
import com.software.modsen.ratingservice.util.RatingTestEntities;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static com.software.modsen.ratingservice.util.RatingTestEntities.DRIVER_RATING;
import static com.software.modsen.ratingservice.util.RatingTestEntities.EXPECTED_RATING_LIST_SIZE;
import static com.software.modsen.ratingservice.util.RatingTestEntities.PASSENGER_RATING;
import static com.software.modsen.ratingservice.util.RatingTestEntities.RATING_BASE_URL;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest
public class RideControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RatingRepository ratingRepository;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    public static void setUp(DynamicPropertyRegistry registry) {
        registry.add(RATING_BASE_URL, wireMockExtension::baseUrl);
    }

    private ObjectMapper objectMapper;

    @BeforeAll
    public static void setUpPostgresDB() {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("rating_test_db")
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
        ratingRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void testGetAllRatings() {
        Rating firstRating = RatingTestEntities.getTestRatingForIT();
        Rating secondRating = RatingTestEntities.getSecondTestRatingForIT();
        ratingRepository.save(firstRating);
        ratingRepository.save(secondRating);

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(RATING_BASE_URL)
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("ratingResponseList", isA(List.class))
                .body("ratingResponseList.size()", is(EXPECTED_RATING_LIST_SIZE))
                .body("ratingResponseList[0].passengerRating", is(PASSENGER_RATING))
                .body("ratingResponseList[1].driverRating", is(DRIVER_RATING));
    }
}
