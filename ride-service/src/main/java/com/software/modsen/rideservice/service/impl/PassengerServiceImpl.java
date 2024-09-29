package com.software.modsen.rideservice.service.impl;

import com.software.modsen.rideservice.client.PassengerClient;
import com.software.modsen.rideservice.dto.response.PassengerResponse;
import com.software.modsen.rideservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerClient passengerClient;

    @Override
    public PassengerResponse getPassengerById(Long passengerId) {
        return passengerClient.getPassengerById(passengerId);
    }
}
