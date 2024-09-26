package com.software.modsen.passengerservice.exception;

public class PassengerNotExistsException extends RuntimeException {
    public PassengerNotExistsException(String message) {
        super(message);
    }
}
