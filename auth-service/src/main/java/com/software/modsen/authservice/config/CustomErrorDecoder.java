package com.software.modsen.authservice.config;

import com.software.modsen.authservice.exception.FeignClientException;
import com.software.modsen.authservice.exception.NotFoundException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        FeignException exception = FeignException.errorStatus(methodKey, response);
        String feignMessage = exception.getMessage();
        String exMessage = feignMessage.substring(feignMessage.indexOf("\"message\":\"") + 11, feignMessage.lastIndexOf("\""));
        return switch (response.status()) {
            case 400 -> new FeignClientException(exMessage);
            case 404 -> new NotFoundException(exception.getMessage());
            default -> exception;
        };
    }
}
