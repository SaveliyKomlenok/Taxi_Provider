package com.software.modsen.rideservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static com.software.modsen.rideservice.util.AuthParameters.AUTH_HEADER;
import static com.software.modsen.rideservice.util.AuthParameters.AUTH_TYPE;

@Configuration
public class FeignConfig implements RequestInterceptor {
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            Jwt jwt = (Jwt) authentication.getCredentials();
            String token = jwt.getTokenValue();
            requestTemplate.header(AUTH_HEADER, AUTH_TYPE + token);
        }
    }
}
