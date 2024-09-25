package com.software.modsen.passengerservice.exception;

import com.software.modsen.passengerservice.exceptionhandler.ExceptionManager;

public class PassengerIsAlreadyExistsException extends ExceptionManager {
    public PassengerIsAlreadyExistsException(String message) {
        super(message);
    }
}
