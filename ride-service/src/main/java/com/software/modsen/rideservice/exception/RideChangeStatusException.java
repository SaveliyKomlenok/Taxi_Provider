package com.software.modsen.rideservice.exception;

import com.software.modsen.rideservice.exceptionhandler.ExceptionManager;

public class RideChangeStatusException extends ExceptionManager {
    public RideChangeStatusException(String message) {
        super(message);
    }
}
