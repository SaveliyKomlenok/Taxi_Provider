package com.software.modsen.ratingservice.exception;

import com.software.modsen.ratingservice.exceptionhandler.ExceptionManager;

public class NotFoundDriverRatingException extends ExceptionManager {
    public NotFoundDriverRatingException(String message) {
        super(message);
    }
}
