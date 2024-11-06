package com.software.modsen.driverservice.util;

import com.software.modsen.driverservice.dto.request.CarChangeStatusRequest;
import com.software.modsen.driverservice.dto.request.CarCreateRequest;
import com.software.modsen.driverservice.dto.request.CarUpdateRequest;
import com.software.modsen.driverservice.entity.Car;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CarTestEntities {
    public static final Long CAR_ID = 1L;
    public static final String CAR_NUMBER = "ABC1234";
    public static final String CAR_KIND = "Toyota";
    public static final String CAR_COLOR = "Red";
    public static final boolean CAR_NOT_RESTRICT = false;
    public static final Long SECOND_CAR_ID = 2L;
    public static final String SECOND_CAR_NUMBER = "XYZ5678";
    public static final Integer PAGE_NUMBER = 0;
    public static final Integer PAGE_SIZE = 10;
    public static final String SORT_BY_ID = "id";
    public static final Integer EXPECTED_LIST_SIZE = 1;
    public static final Integer FIRST_INDEX = 0;
    public static final Integer WANTED_NUMBER_OF_INVOCATIONS = 1;
    public static final String CAR_BASE_URL = "https://localhost:8082/api/v1/cars";

    public Car getTestCar() {
        return Car.builder()
                .id(CAR_ID)
                .number(CAR_NUMBER)
                .kind(CAR_KIND)
                .color(CAR_COLOR)
                .restricted(CAR_NOT_RESTRICT)
                .build();
    }

    public Car getSecondTestCar() {
        return Car.builder()
                .id(SECOND_CAR_ID)
                .number(SECOND_CAR_NUMBER)
                .kind(CAR_KIND)
                .color(CAR_COLOR)
                .restricted(CAR_NOT_RESTRICT)
                .build();
    }

    public CarChangeStatusRequest getCarChangeStatusRequest(Long id) {
        return CarChangeStatusRequest.builder()
                .id(id)
                .status(true)
                .build();
    }

    public Car getCarForIT() {
        return Car.builder()
                .kind(CAR_KIND)
                .color(CAR_COLOR)
                .number(CAR_NUMBER)
                .restricted(CAR_NOT_RESTRICT)
                .build();
    }

    public Car getSecondCarForIT(){
        return Car.builder()
                .number(SECOND_CAR_NUMBER)
                .kind(CAR_KIND)
                .color(CAR_COLOR)
                .restricted(CAR_NOT_RESTRICT)
                .build();
    }

    public CarCreateRequest getCarCreateRequestForIT() {
        return CarCreateRequest.builder()
                .kind(CAR_KIND)
                .color(CAR_COLOR)
                .number(CAR_NUMBER)
                .build();
    }

    public CarUpdateRequest getCarUpdateRequestForIT() {
        return CarUpdateRequest.builder()
                .id(CAR_ID)
                .kind(CAR_KIND)
                .color(CAR_COLOR)
                .number(SECOND_CAR_NUMBER)
                .build();
    }
}
