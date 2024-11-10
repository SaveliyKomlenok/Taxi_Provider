package com.software.modsen.authservice.exception;

public class FeignClientException extends RuntimeException {
    public FeignClientException(String message) {
        super(message);
    }
}
