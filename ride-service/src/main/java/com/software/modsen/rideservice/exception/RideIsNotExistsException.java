package com.software.modsen.rideservice.exception;

import com.software.modsen.rideservice.exceptionhandler.ExceptionManager;

public class RideIsNotExistsException extends ExceptionManager {
    public RideIsNotExistsException(String message) {
        super(message);
    }
}
