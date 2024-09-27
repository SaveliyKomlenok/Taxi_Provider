package com.software.modsen.ratingservice.exception;

public class DriverAlreadyHasRatingException extends RuntimeException {
    public DriverAlreadyHasRatingException(String message) {
        super(message);
    }
}
