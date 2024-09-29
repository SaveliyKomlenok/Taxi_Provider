package com.software.modsen.ratingservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public static final String DRIVER_NOT_RATED = "Driver with id %d is not rated";
    public static final String DRIVER_HAS_RATING = "Driver with id %d already has rating";
}
