package com.software.modsen.ratingservice.util;

import com.software.modsen.ratingservice.dto.request.RatingDriverRequest;
import com.software.modsen.ratingservice.entity.Rating;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RatingTestEntities {
    public static final Long RATING_ID = 1L;
    public static final Long PASSENGER_ID = 2L;
    public static final Long DRIVER_ID = 3L;
    public static final Long RIDE_ID = 4L;
    public static final Integer PASSENGER_RATING = 4;
    public static final Integer DRIVER_RATING = 5;
    public static final Double AVERAGE_PASSENGER_RATING = 4.0;
    public static final Double AVERAGE_DRIVER_RATING = 5.0;
    public static final String COMMENT = "Great driver!";
    public static final Integer PAGE_NUMBER = 0;
    public static final Integer PAGE_SIZE = 10;
    public static final String SORT_BY_ID = "id";
    public static final Integer EXPECTED_LIST_SIZE = 1;
    public static final Integer FIRST_INDEX = 0;

    public Rating getTestRating() {
        return Rating.builder()
                .id(RATING_ID)
                .passengerId(PASSENGER_ID)
                .driverId(DRIVER_ID)
                .passengerRating(PASSENGER_RATING)
                .driverRating(null)
                .comment(COMMENT)
                .build();
    }

    public RatingDriverRequest getDriverRatingRequestForIT() {
        return RatingDriverRequest.builder()
                .rideId(RIDE_ID)
                .driverId(DRIVER_ID)
                .passengerId(PASSENGER_ID)
                .driverRating(DRIVER_RATING)
                .comment(COMMENT)
                .build();
    }
}
