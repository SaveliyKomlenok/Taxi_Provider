package com.software.modsen.rideservice.exception;

public class DriverBusyException extends RuntimeException{
    public DriverBusyException(String message) {
        super(message);
    }
}
