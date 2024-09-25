package com.software.modsen.driverservice.exception;

import com.software.modsen.driverservice.exceptionhandler.ExceptionManager;

public class DriverIsNotExistsException extends ExceptionManager {
    public DriverIsNotExistsException(String message) {
        super(message);
    }
}
