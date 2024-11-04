package com.software.modsen.authservice.service;

import com.software.modsen.authservice.dto.request.AuthenticationRequest;
import com.software.modsen.authservice.dto.request.PassengerRegistrationRequest;
import com.software.modsen.authservice.dto.response.AuthenticationResponse;
import com.software.modsen.authservice.dto.request.DriverRegistrationRequest;
import com.software.modsen.authservice.dto.response.RegistrationResponse;

public interface AuthService {
    AuthenticationResponse login(AuthenticationRequest request);
    RegistrationResponse passengerRegistration(PassengerRegistrationRequest request);
    RegistrationResponse driverRegistration(DriverRegistrationRequest request);
}
