package com.software.modsen.rideservice.exception;

public class DriverRestrictedException extends RuntimeException {
    public DriverRestrictedException(String message) {
        super(message);
    }
}
