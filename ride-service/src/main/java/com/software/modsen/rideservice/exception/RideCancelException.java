package com.software.modsen.rideservice.exception;

import com.software.modsen.rideservice.exceptionhandler.ExceptionManager;

public class RideCancelException extends ExceptionManager {
    public RideCancelException(String message) {
        super(message);
    }
}
