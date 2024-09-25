package com.software.modsen.driverservice.exception;

import com.software.modsen.driverservice.exceptionhandler.ExceptionManager;

public class CarIsOccupiedException extends ExceptionManager {
    public CarIsOccupiedException(String message) {
        super(message);
    }
}
