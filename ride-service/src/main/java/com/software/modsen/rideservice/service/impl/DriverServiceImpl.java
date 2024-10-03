package com.software.modsen.rideservice.service.impl;

import com.software.modsen.rideservice.client.DriverClient;
import com.software.modsen.rideservice.dto.response.DriverResponse;
import com.software.modsen.rideservice.exception.ServiceNotAvailableException;
import com.software.modsen.rideservice.service.DriverService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.software.modsen.rideservice.util.ExceptionMessages.DRIVER_SERVICE_NOT_AVAILABLE;
import static com.software.modsen.rideservice.util.RetryConstants.CHANGE_BUSY_STATUS_DRIVER;
import static com.software.modsen.rideservice.util.RetryConstants.DRIVER_BY_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverClient driverClient;

    @Override
    @Retry(name = DRIVER_BY_ID)
    @CircuitBreaker(name = DRIVER_BY_ID, fallbackMethod = "fallBackGetDriverById")
    public DriverResponse getDriverById(Long driverId) {
        return driverClient.getDriverById(driverId);
    }

    @Override
    @Retry(name = CHANGE_BUSY_STATUS_DRIVER)
    @CircuitBreaker(name = CHANGE_BUSY_STATUS_DRIVER, fallbackMethod = "fallBackChangeBusyStatus")
    public DriverResponse changeBusyStatus(Long driverId) {
        return driverClient.changeBusyStatus(driverId);
    }

    private DriverResponse fallBackGetDriverById(Throwable throwable) {
        log.info(throwable.getMessage());
        throw new ServiceNotAvailableException(DRIVER_SERVICE_NOT_AVAILABLE);
    }

    private DriverResponse fallBackChangeBusyStatus(Throwable throwable) {
        log.info(throwable.getMessage());
        throw new ServiceNotAvailableException(DRIVER_SERVICE_NOT_AVAILABLE);
    }
}
