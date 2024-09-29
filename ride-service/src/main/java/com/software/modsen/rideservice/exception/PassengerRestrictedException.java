package com.software.modsen.rideservice.exception;

public class PassengerRestrictedException extends RuntimeException{
    public PassengerRestrictedException(String message) {
        super(message);
    }
}
