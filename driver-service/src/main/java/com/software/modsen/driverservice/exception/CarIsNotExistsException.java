package com.software.modsen.driverservice.exception;

import com.software.modsen.driverservice.exceptionhandler.ExceptionManager;

public class CarIsNotExistsException extends ExceptionManager {
    public CarIsNotExistsException(String message) {
        super(message);
    }
}
