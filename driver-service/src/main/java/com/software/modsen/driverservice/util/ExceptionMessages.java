package com.software.modsen.driverservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public static final String DRIVER_NOT_EXISTS = "Driver with id %d is not exists";
    public static final String DRIVER_ALREADY_EXISTS = "Driver is already exists";
    public static final String RATING_NOT_EXISTS = "Rating with driver id %d is not exists";
    public static final String CAR_OCCUPIED = "Car with id %d is occupied";
    public static final String CAR_NOT_EXISTS = "Car with id %d is not exists";
    public static final String CAR_ALREADY_EXISTS = "Car is already exists";
}
