package com.software.modsen.passengerservice.exception;

import com.software.modsen.passengerservice.exceptionhandler.ExceptionManager;

public class PassengerRatingIsNotExistsException extends ExceptionManager {
    public PassengerRatingIsNotExistsException(String message) {
        super(message);
    }
}
