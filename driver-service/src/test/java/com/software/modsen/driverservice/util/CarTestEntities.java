package com.software.modsen.driverservice.util;

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
}
