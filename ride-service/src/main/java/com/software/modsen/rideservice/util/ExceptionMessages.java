package com.software.modsen.rideservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessages {
    public static final String RIDE_NOT_EXISTS = "Ride with id %d is not exists";
    public static final String RIDE_NOT_ACCEPTED = "Ride with id %d is not accepted";
    public static final String RIDE_NOT_CANCELED = "Ride with id %d is not canceled";
    public static final String RIDE_STATUS_NOT_CHANGED = "Ride with id %d is not changed";
    public static final String RIDE_NOT_FINISHED = "Ride with id %d is not finished";
    public static final String INVALID_RIDE_STATUS = "Invalid ride status";
    public static final String PASSENGER_RESTRICTED = "Passenger is restricted";
    public static final String DRIVER_BUSY = "Driver is busy with another ride";
    public static final String DRIVER_RESTRICTED = "Driver is restricted";
}
