package com.software.modsen.rideservice.service;

import com.software.modsen.rideservice.dto.request.DriverChangeStatusRequest;
import com.software.modsen.rideservice.dto.response.DriverResponse;

public interface DriverService {
    DriverResponse getDriverById(Long driverId);
    DriverResponse changeBusyStatus(DriverChangeStatusRequest request);
}
