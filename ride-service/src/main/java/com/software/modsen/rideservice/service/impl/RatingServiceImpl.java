package com.software.modsen.rideservice.service.impl;

import com.software.modsen.rideservice.client.RatingClient;
import com.software.modsen.rideservice.dto.request.RatingPassengerRequest;
import com.software.modsen.rideservice.dto.response.RatingResponse;
import com.software.modsen.rideservice.exception.ServiceNotAvailableException;
import com.software.modsen.rideservice.service.RatingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.software.modsen.rideservice.util.ExceptionMessages.RATING_SERVICE_NOT_AVAILABLE;
import static com.software.modsen.rideservice.util.RetryConstants.RATE_BY_DRIVER;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingClient ratingClient;

    @Override
    @Retry(name = RATE_BY_DRIVER)
    @CircuitBreaker(name = RATE_BY_DRIVER, fallbackMethod = "fallBackRateByDriver")
    public RatingResponse rateByDriver(RatingPassengerRequest request) {
        return ratingClient.rateByDriver(request);
    }

    private RatingResponse fallBackRateByDriver(Throwable throwable) {
        log.info(throwable.getMessage());
        throw new ServiceNotAvailableException(RATING_SERVICE_NOT_AVAILABLE);
    }
}
