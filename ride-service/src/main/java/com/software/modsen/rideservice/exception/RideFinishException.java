package com.software.modsen.rideservice.exception;

import com.software.modsen.rideservice.exceptionhandler.ExceptionManager;

public class RideFinishException extends ExceptionManager {
    public RideFinishException(String message) {
        super(message);
    }
}
