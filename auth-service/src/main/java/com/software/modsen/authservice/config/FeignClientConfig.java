package com.software.modsen.authservice.config;

import com.software.modsen.authservice.dto.response.AuthenticationResponse;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.software.modsen.authservice.util.AuthParameters.AUTH_HEADER;
import static com.software.modsen.authservice.util.AuthParameters.AUTH_TYPE;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_ID;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_SECRET;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.GRANT_TYPE;

@Configuration
@RequiredArgsConstructor
public class FeignClientConfig implements RequestInterceptor {
    private final RestTemplate restTemplate;

    @Value("${jwt.auth.client-grant-type}")
    private String clientGrantType;

    @Value("${jwt.auth.client-id}")
    private String clientId;

    @Value("${jwt.auth.client-secret}")
    private String clientSecret;

    @Value("${jwt.auth.token}")
    private String loginUrl;

    @Override
    public void apply(RequestTemplate template) {
        String token = authenticateClient().getAccess_token();
        template.header(AUTH_HEADER, AUTH_TYPE + token);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
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
}
