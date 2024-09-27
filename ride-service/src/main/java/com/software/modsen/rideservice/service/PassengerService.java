package com.software.modsen.rideservice.service;

import com.software.modsen.rideservice.dto.response.PassengerResponse;

public interface PassengerService {
    PassengerResponse getPassengerById(Long passengerId);
}
