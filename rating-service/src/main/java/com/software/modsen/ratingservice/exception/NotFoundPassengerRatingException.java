package com.software.modsen.ratingservice.exception;

import com.software.modsen.ratingservice.exceptionhandler.ExceptionManager;

public class NotFoundPassengerRatingException extends ExceptionManager {
    public NotFoundPassengerRatingException(String message) {
        super(message);
    }
}
