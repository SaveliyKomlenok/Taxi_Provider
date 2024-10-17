package com.software.modsen.passengerservice.util;

import com.software.modsen.passengerservice.dto.request.PassengerCreateRequest;
import com.software.modsen.passengerservice.dto.request.PassengerRatingRequest;
import com.software.modsen.passengerservice.dto.request.PassengerUpdateRequest;
import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.entity.PassengerRating;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestEntities {
    public static final Long PASSENGER_ID = 1L;
    public static final String PASSENGER_FIRSTNAME = "Джон";
    public static final String PASSENGER_SURNAME = "Уильямс";
    public static final String PASSENGER_PATRONYMIC = "Джонсон";
    public static final String PASSENGER_EMAIL = "test@example.com";
    public static final String PASSENGER_PHONE_NUMBER = "+375331234567";
    public static final String SECOND_PASSENGER_EMAIL = "example@test.com";
    public static final String SECOND_PASSENGER_PHONE_NUMBER = "+375337654321";
    public static final Integer PAGE_NUMBER = 0;
    public static final Integer PAGE_SIZE = 10;
    public static final String SORT_BY_ID = "id";
    public static final Integer EXPECTED_LIST_SIZE = 1;
    public static final Integer EXPECTED_PASSENGER_LIST_SIZE = 2;
    public static final Integer FIRST_INDEX = 0;
    public static final Integer WANTED_NUMBER_OF_INVOCATIONS = 1;
    public static final Double PASSENGER_RATING = 4.55;
    public static final Long SECOND_PASSENGER_ID = 2L;
    public static final String PASSENGER_BASE_URL = "http://localhost:8081/api/v1/passengers";
    public static final String PASSENGER_RATING_BASE_URL = "http://localhost:8081/api/v1/passenger-ratings";

    public Passenger getTestPassenger() {
        return Passenger.builder()
                .id(PASSENGER_ID)
                .email(PASSENGER_EMAIL)
                .phoneNumber(PASSENGER_PHONE_NUMBER)
                .restricted(false)
                .build();
    }

    public Passenger getSecondTestPassenger() {
        return Passenger.builder()
                .id(SECOND_PASSENGER_ID)
                .email(PASSENGER_EMAIL)
                .phoneNumber(PASSENGER_PHONE_NUMBER)
                .build();
    }

    public PassengerRating getTestPassengerRating() {
        return PassengerRating.builder()
                .passenger(getTestPassenger())
                .rating(PASSENGER_RATING)
                .build();
    }

    public Passenger getPassengerForIT() {
        return Passenger.builder()
                .email(PASSENGER_EMAIL)
                .phoneNumber(PASSENGER_PHONE_NUMBER)
                .build();
    }

    public Passenger getSecondPassengerForIT() {
        return Passenger.builder()
                .email(SECOND_PASSENGER_EMAIL)
                .phoneNumber(SECOND_PASSENGER_PHONE_NUMBER)
                .build();
    }

    public PassengerCreateRequest getPassengerCreateRequestForIT() {
        return PassengerCreateRequest.builder()
                .firstname(PASSENGER_FIRSTNAME)
                .surname(PASSENGER_SURNAME)
                .patronymic(PASSENGER_PATRONYMIC)
                .email(PASSENGER_EMAIL)
                .phoneNumber(PASSENGER_PHONE_NUMBER)
                .build();
    }

    public PassengerUpdateRequest getPassengerUpdateRequestForIT() {
        return PassengerUpdateRequest.builder()
                .firstname(PASSENGER_FIRSTNAME)
                .surname(PASSENGER_SURNAME)
                .patronymic(PASSENGER_PATRONYMIC)
                .email(SECOND_PASSENGER_EMAIL)
                .phoneNumber(PASSENGER_PHONE_NUMBER)
                .build();
    }

    public PassengerRating getPassengerRatingForIT() {
        return PassengerRating.builder()
                .rating(PASSENGER_RATING)
                .build();
    }

    public PassengerRatingRequest getPassengerRatingRequestForIT() {
        return PassengerRatingRequest.builder()
                .passengerId(PASSENGER_ID)
                .passengerRating(PASSENGER_RATING)
                .build();
    }
}
