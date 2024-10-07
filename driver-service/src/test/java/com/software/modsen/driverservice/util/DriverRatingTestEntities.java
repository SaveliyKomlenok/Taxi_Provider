package com.software.modsen.driverservice.util;

import com.software.modsen.driverservice.entity.DriverRating;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DriverRatingTestEntities {
    public static final double DRIVER_RATING = 4.55;
    public static final double NEW_DRIVER_RATING = 3.66;

    public DriverRating getTestDriverRating() {
        return DriverRating.builder()
                .driver(DriverTestEntities.getTestDriver())
                .rating(DRIVER_RATING)
                .build();
    }

    public DriverRating getTestExistingDriverRating(){
        return DriverRating.builder()
                .driver(DriverTestEntities.getTestDriver())
                .rating(NEW_DRIVER_RATING)
                .build();
    }
}
