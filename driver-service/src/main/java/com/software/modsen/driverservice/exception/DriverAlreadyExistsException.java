package com.software.modsen.driverservice.exception;

public class DriverAlreadyExistsException extends RuntimeException{
    public DriverAlreadyExistsException(String message) {
        super(message);
    }
}
