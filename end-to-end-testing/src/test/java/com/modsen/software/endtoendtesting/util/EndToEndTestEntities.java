package com.modsen.software.endtoendtesting.util;

import com.modsen.software.endtoendtesting.dto.request.CarCreateRequest;
import com.modsen.software.endtoendtesting.dto.request.DriverCreateRequest;
import com.modsen.software.endtoendtesting.dto.request.PassengerCreateRequest;
import com.modsen.software.endtoendtesting.dto.request.RideCreateRequest;
import com.modsen.software.endtoendtesting.dto.request.RideFinishRequest;
import com.modsen.software.endtoendtesting.dto.request.RideStatusChangeRequest;
import com.modsen.software.endtoendtesting.dto.response.CarResponse;
import com.modsen.software.endtoendtesting.dto.response.DriverResponse;
import com.modsen.software.endtoendtesting.dto.response.PassengerResponse;
import com.modsen.software.endtoendtesting.dto.response.RideResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EndToEndTestEntities {
    public static final String PASSENGER_FIRSTNAME = "Джон";
    public static final String PASSENGER_SURNAME = "Уильямс";
    public static final String PASSENGER_PATRONYMIC = "Джонсон";
    public static final String PASSENGER_EMAIL = "test@example.com";
    public static final String PASSENGER_PHONE_NUMBER = "+375331234567";

    public static final String CAR_NUMBER = "ABC1234";
    public static final String CAR_KIND = "Toyota";
    public static final String CAR_COLOR = "Red";

    public static final String DRIVER_FIRSTNAME = "Джонсон";
    public static final String DRIVER_SURNAME = "Джон";
    public static final String DRIVER_PATRONYMIC = "Уильямс";
    public static final String DRIVER_EMAIL = "example@test.com";
    public static final String DRIVER_PHONE_NUMBER = "+375337654321";

    public static final String ADDRESS_FROM = "Минск";
    public static final String ADDRESS_TO = "Брест";
    public static final Integer PASSENGER_RATING = 5;

    public PassengerCreateRequest getPassengerCreateRequest() {
        return PassengerCreateRequest.builder()
                .firstname(PASSENGER_FIRSTNAME)
                .surname(PASSENGER_SURNAME)
                .patronymic(PASSENGER_PATRONYMIC)
                .email(PASSENGER_EMAIL)
                .phoneNumber(PASSENGER_PHONE_NUMBER)
                .build();
    }

    public CarCreateRequest getCarCreateRequest() {
        return CarCreateRequest.builder()
                .number(CAR_NUMBER)
                .kind(CAR_KIND)
                .color(CAR_COLOR)
                .build();
    }

    public DriverCreateRequest getDriverCreateRequest(CarResponse car) {
        return DriverCreateRequest.builder()
                .car(car.getId())
                .firstname(DRIVER_FIRSTNAME)
                .surname(DRIVER_SURNAME)
                .patronymic(DRIVER_PATRONYMIC)
                .email(DRIVER_EMAIL)
                .phoneNumber(DRIVER_PHONE_NUMBER)
                .build();
    }

    public RideCreateRequest getRideCreateRequest(PassengerResponse passenger) {
        return RideCreateRequest.builder()
                .passengerId(passenger.getId())
                .addressFrom(ADDRESS_FROM)
                .addressTo(ADDRESS_TO)
                .build();
    }

    public RideStatusChangeRequest getRideStatusChangeRequest(RideResponse ride, DriverResponse driver) {
        return RideStatusChangeRequest.builder()
                .rideId(ride.getId())
                .driverId(driver.getId())
                .build();
    }

    public RideFinishRequest getRideFinishRequest(RideResponse ride, DriverResponse driver){
        return RideFinishRequest.builder()
                .rideId(ride.getId())
                .driverId(driver.getId())
                .passengerRating(PASSENGER_RATING)
                .build();
    }
}
