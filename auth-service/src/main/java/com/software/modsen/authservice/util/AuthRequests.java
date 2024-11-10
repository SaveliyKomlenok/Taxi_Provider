package com.software.modsen.authservice.util;

import com.software.modsen.authservice.dto.request.DriverCreateRequest;
import com.software.modsen.authservice.dto.request.DriverRegistrationRequest;
import com.software.modsen.authservice.dto.request.KeycloakRegistrationRequest;
import com.software.modsen.authservice.dto.request.PassengerCreateRequest;
import com.software.modsen.authservice.dto.request.PassengerRegistrationRequest;
import com.software.modsen.authservice.dto.response.RegistrationResponse;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class AuthRequests {
    public DriverCreateRequest getDriverCreateRequest(DriverRegistrationRequest request) {
        return DriverCreateRequest.builder()
                .firstname(request.getFirstname())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build();
    }

    public PassengerCreateRequest getPassengerCreateRequest(PassengerRegistrationRequest request) {
        return PassengerCreateRequest.builder()
                .firstname(request.getFirstname())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build();
    }

    public KeycloakRegistrationRequest getKeycloakDriverRegistrationRequest(DriverRegistrationRequest request,
                                                                            Map<String, String> attributes,
                                                                            List<KeycloakRegistrationRequest.Credential> credentials) {
        return KeycloakRegistrationRequest.builder()
                .attributes(attributes)
                .credentials(credentials)
                .username(request.getUsername())
                .firstName(request.getFirstname())
                .lastName(request.getSurname())
                .email(request.getEmail())
                .emailVerified(false)
                .enabled(true)
                .build();
    }

    public KeycloakRegistrationRequest getKeycloakPassengerRegistrationRequest(PassengerRegistrationRequest request,
                                                                               Map<String, String> attributes,
                                                                               List<KeycloakRegistrationRequest.Credential> credentials) {
        return KeycloakRegistrationRequest.builder()
                .attributes(attributes)
                .credentials(credentials)
                .username(request.getUsername())
                .firstName(request.getFirstname())
                .lastName(request.getSurname())
                .email(request.getEmail())
                .emailVerified(false)
                .enabled(true)
                .build();
    }

    public RegistrationResponse buildDriverRegistrationResponse(DriverRegistrationRequest request) {
        return RegistrationResponse.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build();
    }

    public RegistrationResponse buildPassengerRegistrationResponse(PassengerRegistrationRequest request) {
        return RegistrationResponse.builder()
                .username(request.getUsername())
                .firstname(request.getFirstname())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build();
    }
}
