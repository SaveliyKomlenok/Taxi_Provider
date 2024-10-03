package com.software.modsen.rideservice.service.impl;

import com.software.modsen.rideservice.client.PassengerClient;
import com.software.modsen.rideservice.dto.response.PassengerResponse;
import com.software.modsen.rideservice.exception.ServiceNotAvailableException;
import com.software.modsen.rideservice.service.PassengerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.software.modsen.rideservice.util.ExceptionMessages.PASSENGER_SERVICE_NOT_AVAILABLE;
import static com.software.modsen.rideservice.util.RetryConstants.PASSENGER_BY_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerClient passengerClient;

    @Override
    @Retry(name = PASSENGER_BY_ID)
    @CircuitBreaker(name = PASSENGER_BY_ID, fallbackMethod = "fallBackGetPassengerById")
    public PassengerResponse getPassengerById(Long passengerId) {
        return passengerClient.getPassengerById(passengerId);
    }

    private PassengerResponse fallBackGetPassengerById(Throwable throwable) {
        log.info(throwable.getMessage());
        throw new ServiceNotAvailableException(PASSENGER_SERVICE_NOT_AVAILABLE);
    }
}
