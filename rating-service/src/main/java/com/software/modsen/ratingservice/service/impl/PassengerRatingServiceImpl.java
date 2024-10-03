package com.software.modsen.ratingservice.service.impl;

import com.software.modsen.ratingservice.client.PassengerClient;
import com.software.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.software.modsen.ratingservice.dto.response.PassengerRatingResponse;
import com.software.modsen.ratingservice.exception.ServiceNotAvailableException;
import com.software.modsen.ratingservice.service.PassengerRatingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.software.modsen.ratingservice.util.ExceptionMessages.DRIVER_SERVICE_NOT_AVAILABLE;
import static com.software.modsen.ratingservice.util.RetryConstants.SAVE_PASSENGER_RATING;

@Service
@Slf4j
@RequiredArgsConstructor
public class PassengerRatingServiceImpl implements PassengerRatingService {
    private final PassengerClient passengerClient;

    @Override
    @Retry(name = SAVE_PASSENGER_RATING)
    @CircuitBreaker(name = SAVE_PASSENGER_RATING, fallbackMethod = "fallBackSavePassengerRating")
    public PassengerRatingResponse savePassengerRating(PassengerRatingRequest request) {
        return passengerClient.savePassengerRating(request);
    }

    private PassengerRatingResponse fallBackSavePassengerRating(Throwable throwable) {
        log.info(throwable.getMessage());
        throw new ServiceNotAvailableException(DRIVER_SERVICE_NOT_AVAILABLE);
    }
}
