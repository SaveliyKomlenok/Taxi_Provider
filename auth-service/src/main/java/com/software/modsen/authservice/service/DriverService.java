package com.software.modsen.authservice.service;

import com.software.modsen.authservice.dto.request.DriverCreateRequest;
import com.software.modsen.authservice.dto.response.DriverResponse;

public interface DriverService {
    DriverResponse save(DriverCreateRequest request);
}
