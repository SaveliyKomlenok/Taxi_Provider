package com.software.modsen.driverservice.util;

import com.software.modsen.driverservice.dto.request.DriverCreateRequest;
import com.software.modsen.driverservice.dto.request.DriverUpdateRequest;
import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.entity.Driver;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DriverTestEntities {
    public static final Long DRIVER_ID = 1L;
    public static final String DRIVER_EMAIL = "driver@example.com";
    public static final String DRIVER_PHONE_NUMBER = "+375331234567";
    public static final boolean DRIVER_NOT_RESTRICT = false;
    public static final boolean DRIVER_NOT_BUSY = false;
    public static final Long CAR_ID = 1L;
    public static final Long SECOND_DRIVER_ID = 2L;
    public static final String SECOND_DRIVER_EMAIL = "driver@example.com";
    public static final String SECOND_DRIVER_PHONE_NUMBER = "+375331234567";
    public static final String CAR_KIND = "Toyota";
    public static final Integer PAGE_NUMBER = 0;
    public static final Integer PAGE_SIZE = 10;
    public static final String SORT_BY_ID = "id";
    public static final Integer EXPECTED_LIST_SIZE = 1;
    public static final Integer FIRST_INDEX = 0;
    public static final Integer WANTED_NUMBER_OF_INVOCATIONS = 1;
    public static final String DRIVER_BASE_URL = "http://localhost:8082/api/v1/drivers";

    public Driver getTestDriver() {
        Car car = Car.builder()
                .id(CAR_ID)
                .kind(CAR_KIND)
                .build();

        return Driver.builder()
                .email(DRIVER_EMAIL)
                .phoneNumber(DRIVER_PHONE_NUMBER)
                .car(car)
                .restricted(DRIVER_NOT_RESTRICT)
                .busy(DRIVER_NOT_BUSY)
                .build();
    }

    public Driver getSecondTestDriver() {
        return Driver.builder()
                .id(SECOND_DRIVER_ID)
                .email(SECOND_DRIVER_EMAIL)
                .phoneNumber(SECOND_DRIVER_PHONE_NUMBER)
                .restricted(DRIVER_NOT_RESTRICT)
                .busy(DRIVER_NOT_BUSY)
                .build();
    }

    public Driver getDriverForIT() {
        return Driver.builder()
                .email(DRIVER_EMAIL)
                .phoneNumber(DRIVER_PHONE_NUMBER)
                .restricted(DRIVER_NOT_RESTRICT)
                .busy(DRIVER_NOT_BUSY)
                .build();
    }

    public Driver getSecondDriverForIT() {
        return Driver.builder()
                .email(SECOND_DRIVER_EMAIL)
                .phoneNumber(SECOND_DRIVER_PHONE_NUMBER)
                .restricted(DRIVER_NOT_RESTRICT)
                .busy(DRIVER_NOT_BUSY)
                .build();
    }

    public DriverCreateRequest getDriverCreateRequestForIT() {
        return DriverCreateRequest.builder()
                .email(DRIVER_EMAIL)
                .phoneNumber(DRIVER_PHONE_NUMBER)
                .build();
    }

    public DriverUpdateRequest getDriverUpdateRequestForIT() {
        return DriverUpdateRequest.builder()
                .email(DRIVER_EMAIL)
                .phoneNumber(DRIVER_PHONE_NUMBER)
                .build();
    }
}
