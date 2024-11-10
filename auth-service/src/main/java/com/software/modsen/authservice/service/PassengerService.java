package com.software.modsen.authservice.service;

import com.software.modsen.authservice.dto.request.PassengerCreateRequest;
import com.software.modsen.authservice.dto.response.PassengerResponse;

public interface PassengerService {
    PassengerResponse save(PassengerCreateRequest request);
}
