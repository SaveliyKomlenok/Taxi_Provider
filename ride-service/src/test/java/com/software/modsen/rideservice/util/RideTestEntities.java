package com.software.modsen.rideservice.util;

import com.software.modsen.rideservice.dto.request.RideCancelRequest;
import com.software.modsen.rideservice.dto.request.RideFinishRequest;
import com.software.modsen.rideservice.dto.request.RideStatusChangeRequest;
import com.software.modsen.rideservice.dto.response.DriverResponse;
import com.software.modsen.rideservice.dto.response.PassengerResponse;
import com.software.modsen.rideservice.entity.Ride;
import com.software.modsen.rideservice.enumiration.Status;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@UtilityClass
public class RideTestEntities {
    public static final Long RIDE_ID = 1L;
    public static final Long PASSENGER_ID = 2L;
    public static final Long DRIVER_ID = 3L;
    public static final Status RIDE_STATUS = Status.CREATED;
    public static final BigDecimal RIDE_PRICE = BigDecimal.valueOf(9.99);
    public static final LocalDateTime NOW = LocalDateTime.now();
    public static final Integer PAGE_NUMBER = 0;
    public static final Integer PAGE_SIZE = 10;
    public static final String SORT_BY_ID = "id";
    public static final Integer EXPECTED_LIST_SIZE = 1;
    public static final Integer FIRST_INDEX = 0;
    public static final String DEFAULT_EMAIL = "test@exapmle.com";
    public static final String DEFAULT_PHONE_NUMBER = "+375291234567";
    public static final Integer PASSENGER_RATING = 4;
    public static final boolean PASSENGER_RESTRICT = true;


    public Ride getTestRide() {
        return Ride.builder()
                .id(RIDE_ID)
                .passengerId(PASSENGER_ID)
                .driverId(DRIVER_ID)
                .status(RIDE_STATUS)
                .startDateTime(NOW)
                .price(RIDE_PRICE)
                .build();
    }

    public PassengerResponse getTestPassenger() {
        return PassengerResponse.builder()
                .id(PASSENGER_ID)
                .email(DEFAULT_EMAIL)
                .phoneNumber(DEFAULT_PHONE_NUMBER)
                .isRestricted(false)
                .build();
    }

    public PassengerResponse getTestRestrictedPassenger() {
        return PassengerResponse.builder()
                .id(PASSENGER_ID)
                .email(DEFAULT_EMAIL)
                .phoneNumber(DEFAULT_PHONE_NUMBER)
                .isRestricted(PASSENGER_RESTRICT)
                .build();
    }

    public DriverResponse getTestDriver() {
        return DriverResponse.builder()
                .id(DRIVER_ID)
                .email(DEFAULT_EMAIL)
                .phoneNumber(DEFAULT_PHONE_NUMBER)
                .build();
    }

    public RideFinishRequest getTestRideFinishRequest() {
        return RideFinishRequest.builder()
                .rideId(RIDE_ID)
                .driverId(DRIVER_ID)
                .passengerRating(PASSENGER_RATING)
                .build();
    }

    public RideCancelRequest getTestRideCancelRequest() {
        return RideCancelRequest.builder()
                .rideId(RIDE_ID)
                .passengerId(PASSENGER_ID)
                .build();
    }

    public RideStatusChangeRequest getTestRideStatusChangeRequest() {
        return RideStatusChangeRequest.builder()
                .rideId(RIDE_ID)
                .driverId(DRIVER_ID)
                .build();
    }
}
