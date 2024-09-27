package com.software.modsen.rideservice.exception;

public class RideNotExistsException extends RuntimeException {
    public RideNotExistsException(String message) {
        super(message);
    }
}
