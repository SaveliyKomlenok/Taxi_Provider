package com.software.modsen.authservice.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.authservice.dto.request.AuthenticationRequest;
import com.software.modsen.authservice.dto.request.DriverCreateRequest;
import com.software.modsen.authservice.dto.request.DriverRegistrationRequest;
import com.software.modsen.authservice.dto.request.KeycloakRegistrationRequest;
import com.software.modsen.authservice.dto.request.PassengerRegistrationRequest;
import com.software.modsen.authservice.dto.response.AuthenticationResponse;
import com.software.modsen.authservice.dto.response.DriverResponse;
import com.software.modsen.authservice.dto.response.RegistrationResponse;
import com.software.modsen.authservice.dto.response.RoleResponse;
import com.software.modsen.authservice.dto.response.UserResponse;
import com.software.modsen.authservice.service.AuthService;
import com.software.modsen.authservice.service.DriverService;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.software.modsen.authservice.util.AuthParameters.AUTH_HEADER;
import static com.software.modsen.authservice.util.AuthParameters.DRIVER_ID_ATTRIBUTE;
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
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${jwt.auth.register-url}")
    private String registerUrl;

    @Value("${jwt.auth.get-driver-role-url}")
    private String getDriverRoleUrl;

    @Value("${jwt.auth.get-user-url}")
    private String getUserUrl;

    @Value("${jwt.auth.map-role-url}")
    private String mapRoleUrl;

    @Value("${jwt.auth.token}")
    private String loginUrl;

    @Value("${jwt.auth.grant-type}")
    private String grantType;

    @Value("${jwt.auth.client-id}")
    private String clientId;

    @Value("${jwt.auth.client-secret}")
    private String clientSecret;

    public static String getBearerTokenHeader() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader(AUTH_HEADER);
    }

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

        return null;
    }

    @Override
    public RegistrationResponse driverRegistration(DriverRegistrationRequest request) {
        HttpHeaders headers = createAuthHeaders();
        DriverResponse driverResponse = registerDriver(request);

        KeycloakRegistrationRequest keycloakRequest = createKeycloakRegistrationRequest(request, driverResponse);
        HttpEntity<KeycloakRegistrationRequest> registrationRequest = new HttpEntity<>(keycloakRequest, headers);
        restTemplate.exchange(registerUrl, HttpMethod.POST, registrationRequest, RegistrationResponse.class);

        mapRoleToUser(getUser(request.getUsername()).getId());
        return AuthRequests.buildRegistrationResponse(request);
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTH_HEADER, getBearerTokenHeader());
        return headers;
    }

    private DriverResponse registerDriver(DriverRegistrationRequest request) {
        DriverCreateRequest driverCreateRequest = AuthRequests.getDriverRegistrationRequest(request);
        return driverService.save(driverCreateRequest);
    }

    private KeycloakRegistrationRequest createKeycloakRegistrationRequest(DriverRegistrationRequest request, DriverResponse driverResponse) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(DRIVER_ID_ATTRIBUTE, driverResponse.getId().toString());

        List<KeycloakRegistrationRequest.Credential> credentials = Collections.singletonList(
                KeycloakRegistrationRequest.Credential.builder()
                        .temporary(false)
                        .type(PASSWORD)
                        .value(request.getPassword())
                        .build()
        );

        return AuthRequests.getKeycloakRegistrationRequest(request, attributes, credentials);
    }

    private RoleResponse getDriverRole() {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(getDriverRoleUrl, HttpMethod.GET, entity, RoleResponse.class).getBody();
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

    private void mapRoleToUser(String userId) {
        HttpHeaders headers = createAuthHeaders();
        List<RoleResponse> rolesList = Collections.singletonList(getDriverRole());

        HttpEntity<List<RoleResponse>> mapRequest = new HttpEntity<>(rolesList, headers);
        restTemplate.exchange(String.format(mapRoleUrl, userId), HttpMethod.POST, mapRequest, String.class);
    }
}