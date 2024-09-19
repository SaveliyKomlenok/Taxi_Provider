package com.software.modsen.passengerservice.exception;

import com.software.modsen.passengerservice.exceptionhandler.ExceptionManager;

public class PassengerIsNotExistsException extends ExceptionManager {
    public PassengerIsNotExistsException(String message) {
        super(message);
    }
}
