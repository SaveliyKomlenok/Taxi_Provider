package com.software.modsen.authservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.authservice.dto.request.AuthenticationRequest;
import com.software.modsen.authservice.dto.request.DriverCreateRequest;
import com.software.modsen.authservice.dto.request.DriverRegistrationRequest;
import com.software.modsen.authservice.dto.request.KeycloakRegistrationRequest;
import com.software.modsen.authservice.dto.request.PassengerCreateRequest;
import com.software.modsen.authservice.dto.request.PassengerRegistrationRequest;
import com.software.modsen.authservice.dto.response.AuthenticationResponse;
import com.software.modsen.authservice.dto.response.DriverResponse;
import com.software.modsen.authservice.dto.response.PassengerResponse;
import com.software.modsen.authservice.dto.response.RegistrationResponse;
import com.software.modsen.authservice.dto.response.RoleResponse;
import com.software.modsen.authservice.dto.response.UserResponse;
import com.software.modsen.authservice.service.AuthService;
import com.software.modsen.authservice.service.DriverService;
import com.software.modsen.authservice.service.PassengerService;
import com.software.modsen.authservice.util.AuthRequests;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.software.modsen.authservice.util.AuthParameters.AUTH_HEADER;
import static com.software.modsen.authservice.util.AuthParameters.AUTH_TYPE;
import static com.software.modsen.authservice.util.AuthParameters.USER_ID_ATTRIBUTE;
import static com.software.modsen.authservice.util.AuthParameters.USER;
import static com.software.modsen.authservice.util.AuthParameters.USER_SEARCH_PARAM;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_ID;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_SECRET;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.GRANT_TYPE;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.PASSWORD;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.USERNAME;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final DriverService driverService;
    private final PassengerService passengerService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${jwt.auth.register-url}")
    private String registerUrl;

    @Value("${jwt.auth.get-driver-role-url}")
    private String getDriverRoleUrl;

    @Value("${jwt.auth.get-passenger-role-url}")
    private String getPassengerRoleUrl;

    @Value("${jwt.auth.get-user-url}")
    private String getUserUrl;

    @Value("${jwt.auth.map-role-url}")
    private String mapRoleUrl;

    @Value("${jwt.auth.token}")
    private String loginUrl;

    @Value("${jwt.auth.grant-type}")
    private String grantType;

    @Value("${jwt.auth.client-grant-type}")
    private String clientGrantType;

    @Value("${jwt.auth.client-id}")
    private String clientId;

    @Value("${jwt.auth.client-secret}")
    private String clientSecret;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        HttpEntity<MultiValueMap<String, String>> entity = createLoginRequestEntity(request);
        return restTemplate.exchange(loginUrl, HttpMethod.POST, entity, AuthenticationResponse.class).getBody();
    }

    private HttpEntity<MultiValueMap<String, String>> createLoginRequestEntity(AuthenticationRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(GRANT_TYPE, grantType);
        map.add(CLIENT_ID, clientId);
        map.add(CLIENT_SECRET, clientSecret);
        map.add(USERNAME, request.getUsername());
        map.add(PASSWORD, request.getPassword());

        return new HttpEntity<>(map, headers);
    }

    @Override
    public RegistrationResponse passengerRegistration(PassengerRegistrationRequest request) {
        HttpHeaders headers = createAuthHeaders();
        PassengerResponse passengerResponse = registerPassenger(request);

        KeycloakRegistrationRequest keycloakRequest = createKeycloakRegistrationRequest(request, passengerResponse);
        HttpEntity<KeycloakRegistrationRequest> registrationRequest = new HttpEntity<>(keycloakRequest, headers);
        restTemplate.exchange(registerUrl, HttpMethod.POST, registrationRequest, RegistrationResponse.class);

        mapRoleToUser(getUser(request.getUsername()).getId(), getPassengerRoleUrl);
        return AuthRequests.buildPassengerRegistrationResponse(request);
    }

    @Override
    public RegistrationResponse driverRegistration(DriverRegistrationRequest request) {
        HttpHeaders headers = createAuthHeaders();
        DriverResponse driverResponse = registerDriver(request);

        KeycloakRegistrationRequest keycloakRequest = createKeycloakRegistrationRequest(request, driverResponse);
        HttpEntity<KeycloakRegistrationRequest> registrationRequest = new HttpEntity<>(keycloakRequest, headers);
        restTemplate.exchange(registerUrl, HttpMethod.POST, registrationRequest, RegistrationResponse.class);

        mapRoleToUser(getUser(request.getUsername()).getId(), getDriverRoleUrl);
        return AuthRequests.buildDriverRegistrationResponse(request);
    }

    private AuthenticationResponse authenticateClient() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(GRANT_TYPE, clientGrantType);
        map.add(CLIENT_ID, clientId);
        map.add(CLIENT_SECRET, clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        return restTemplate.exchange(loginUrl, HttpMethod.POST, entity, AuthenticationResponse.class).getBody();
    }

    private HttpHeaders createAuthHeaders() {
        AuthenticationResponse authenticationResponse = authenticateClient();
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER, AUTH_TYPE + authenticationResponse.getAccess_token());
        return headers;
    }

    private DriverResponse registerDriver(DriverRegistrationRequest request) {
        DriverCreateRequest driverCreateRequest = AuthRequests.getDriverCreateRequest(request);
        return driverService.save(driverCreateRequest);
    }

    private PassengerResponse registerPassenger(PassengerRegistrationRequest request) {
        PassengerCreateRequest passengerCreateRequest = AuthRequests.getPassengerCreateRequest(request);
        return passengerService.save(passengerCreateRequest);
    }

    private KeycloakRegistrationRequest createKeycloakRegistrationRequest(DriverRegistrationRequest request, DriverResponse driverResponse) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(USER_ID_ATTRIBUTE, driverResponse.getId().toString());

        List<KeycloakRegistrationRequest.Credential> credentials = Collections.singletonList(
                KeycloakRegistrationRequest.Credential.builder()
                        .temporary(false)
                        .type(PASSWORD)
                        .value(request.getPassword())
                        .build()
        );

        return AuthRequests.getKeycloakDriverRegistrationRequest(request, attributes, credentials);
    }

    private KeycloakRegistrationRequest createKeycloakRegistrationRequest(PassengerRegistrationRequest request, PassengerResponse passengerResponse) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(USER_ID_ATTRIBUTE, passengerResponse.getId().toString());

        List<KeycloakRegistrationRequest.Credential> credentials = Collections.singletonList(
                KeycloakRegistrationRequest.Credential.builder()
                        .temporary(false)
                        .type(PASSWORD)
                        .value(request.getPassword())
                        .build()
        );

        return AuthRequests.getKeycloakPassengerRegistrationRequest(request, attributes, credentials);
    }

    private RoleResponse getUserRole(String urlForMapRole) {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(urlForMapRole, HttpMethod.GET, entity, RoleResponse.class).getBody();
    }

    @SneakyThrows
    private UserResponse getUser(String username) {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String json = restTemplate.exchange(getUserUrl + USER_SEARCH_PARAM + username, HttpMethod.GET, entity, String.class).getBody();
        List<UserResponse> userList = objectMapper.readValue(json, new TypeReference<>() {
        });

        return userList.get(USER);
    }

    private void mapRoleToUser(String userId, String urlForMapRole) {
        HttpHeaders headers = createAuthHeaders();
        List<RoleResponse> rolesList = Collections.singletonList(getUserRole(urlForMapRole));

        HttpEntity<List<RoleResponse>> mapRequest = new HttpEntity<>(rolesList, headers);
        restTemplate.exchange(String.format(mapRoleUrl, userId), HttpMethod.POST, mapRequest, String.class);
    }
}