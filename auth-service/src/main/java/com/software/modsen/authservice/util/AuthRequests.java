package com.software.modsen.authservice.util;

import com.software.modsen.authservice.dto.request.DriverCreateRequest;
import com.software.modsen.authservice.dto.request.DriverRegistrationRequest;
import com.software.modsen.authservice.dto.request.KeycloakRegistrationRequest;
import com.software.modsen.authservice.dto.response.RegistrationResponse;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class AuthRequests {
    public DriverCreateRequest getDriverRegistrationRequest(DriverRegistrationRequest request) {
        return DriverCreateRequest.builder()
                .firstname(request.getFirstname())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .build();
    }

    public KeycloakRegistrationRequest getKeycloakRegistrationRequest(DriverRegistrationRequest request,
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

    public RegistrationResponse buildRegistrationResponse(DriverRegistrationRequest request) {
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
