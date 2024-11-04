package com.software.modsen.authservice.controller;

import com.software.modsen.authservice.dto.request.AuthenticationRequest;
import com.software.modsen.authservice.dto.request.PassengerRegistrationRequest;
import com.software.modsen.authservice.dto.response.AuthenticationResponse;
import com.software.modsen.authservice.dto.request.DriverRegistrationRequest;
import com.software.modsen.authservice.dto.response.RegistrationResponse;
import com.software.modsen.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }

    @PostMapping("/register/passenger")
    public ResponseEntity<RegistrationResponse> registration(@Valid @RequestBody PassengerRegistrationRequest request){
        return new ResponseEntity<>(authService.passengerRegistration(request), HttpStatus.CREATED);
    }

    @PostMapping("/register/driver")
    public ResponseEntity<RegistrationResponse> registration(@Valid @RequestBody DriverRegistrationRequest request){
        return new ResponseEntity<>(authService.driverRegistration(request), HttpStatus.CREATED);
    }
}
