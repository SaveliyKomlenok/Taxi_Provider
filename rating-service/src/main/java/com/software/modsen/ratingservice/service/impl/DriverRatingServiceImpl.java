package com.software.modsen.ratingservice.service.impl;

import com.software.modsen.ratingservice.client.DriverClient;
import com.software.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.software.modsen.ratingservice.dto.response.DriverRatingResponse;
import com.software.modsen.ratingservice.exception.ServiceNotAvailableException;
import com.software.modsen.ratingservice.service.DriverRatingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.software.modsen.ratingservice.util.ExceptionMessages.DRIVER_SERVICE_NOT_AVAILABLE;
import static com.software.modsen.ratingservice.util.RetryConstants.SAVE_DRIVER_RATING;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverRatingServiceImpl implements DriverRatingService {
    private final DriverClient driverClient;

    @Override
    @Retry(name = SAVE_DRIVER_RATING)
    @CircuitBreaker(name = SAVE_DRIVER_RATING, fallbackMethod = "fallBackSaveDriverRating")
    public DriverRatingResponse saveDriverRating(DriverRatingRequest request) {
        return driverClient.saveDriverRating(request);
    }

    private DriverRatingResponse fallBackSaveDriverRating(Throwable throwable) {
        log.info(throwable.getMessage());
        throw new ServiceNotAvailableException(DRIVER_SERVICE_NOT_AVAILABLE);
    }
}
