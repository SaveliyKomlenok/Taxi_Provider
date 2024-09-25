package com.software.modsen.ratingservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public static final String DRIVER_NOT_RATED = "Driver with id %d is not rated";
    public static final String NOT_FOUND_PASSENGER_RATING = "No ratings found for passenger with id %d";
    public static final String NOT_FOUND_DRIVER_RATING = "No ratings found for driver with id %d";
}
