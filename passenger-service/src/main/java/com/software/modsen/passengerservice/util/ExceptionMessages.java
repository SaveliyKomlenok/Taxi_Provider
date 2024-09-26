package com.software.modsen.passengerservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public static final String PASSENGER_NOT_EXISTS = "Passenger with id %d is not exists";
    public static final String PASSENGER_ALREADY_EXISTS = "Passenger is already exists";
    public static final String RATING_NOT_EXISTS = "Rating with passenger id %d is not exists";
}
