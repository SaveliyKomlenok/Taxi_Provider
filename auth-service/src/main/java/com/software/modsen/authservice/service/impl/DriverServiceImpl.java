package com.software.modsen.authservice.service.impl;

import com.software.modsen.authservice.client.DriverClient;
import com.software.modsen.authservice.dto.request.DriverCreateRequest;
import com.software.modsen.authservice.dto.response.DriverResponse;
import com.software.modsen.authservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverClient driverClient;

    @Override
    public DriverResponse save(DriverCreateRequest request) {
        return driverClient.save(request);
    }
}
