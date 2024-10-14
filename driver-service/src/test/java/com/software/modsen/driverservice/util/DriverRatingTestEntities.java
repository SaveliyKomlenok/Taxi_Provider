package com.software.modsen.driverservice.util;

import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.entity.DriverRating;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DriverRatingTestEntities {
    public static final double DRIVER_RATING = 4.55;
    public static final Integer EXPECTED_LIST_SIZE = 1;
    public static final Long DRIVER_RATING_ID = 1L;
    public static final Integer FIRST_INDEX = 0;
    public static final String DRIVER_RATING_BASE_URL = "http://localhost:8082/api/v1/driver-ratings";

    public DriverRating getTestDriverRating() {
        return DriverRating.builder()
                .driver(DriverTestEntities.getTestDriver())
                .rating(DRIVER_RATING)
                .build();
    }

    public DriverRating getTestDriverRatingForComponent() {
        return DriverRating.builder()
                .id(DRIVER_RATING_ID)
                .driver(DriverTestEntities.getTestDriverForComponent())
                .rating(DRIVER_RATING)
                .build();
    }

    public DriverRatingRequest getTestDriverRatingRequest() {
        return DriverRatingRequest.builder()
                .driverRating(DRIVER_RATING)
                .build();
    }
}
