package com.software.modsen.authservice.service.impl;

import com.software.modsen.authservice.client.PassengerClient;
import com.software.modsen.authservice.dto.request.PassengerCreateRequest;
import com.software.modsen.authservice.dto.response.PassengerResponse;
import com.software.modsen.authservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerClient passengerClient;

    @Override
    public PassengerResponse save(PassengerCreateRequest request) {
        return passengerClient.save(request);
    }
}
