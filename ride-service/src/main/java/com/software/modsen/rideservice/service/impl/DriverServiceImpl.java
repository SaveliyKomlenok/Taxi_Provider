package com.software.modsen.rideservice.service.impl;

import com.software.modsen.rideservice.client.DriverClient;
import com.software.modsen.rideservice.dto.response.DriverResponse;
import com.software.modsen.rideservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverClient driverClient;

    @Override
    public DriverResponse getDriverById(Long driverId) {
        return driverClient.getDriverById(driverId);
    }

    @Override
    public DriverResponse changeBusyStatus(Long driverId) {
        return driverClient.changeBusyStatus(driverId);
    }
}
