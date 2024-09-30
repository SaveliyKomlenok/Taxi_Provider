package com.software.modsen.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("passenger-service", route -> route.path("/api/v1/passengers/**", "/api/v1/passenger-ratings/**")
                        .uri("lb://passenger-service"))
                .route("driver-service", route -> route.path("/api/v1/drivers/**", "/api/v1/cars/**", "/api/v1/driver-ratings/**")
                        .uri("lb://driver-service"))
                .route("rating-service", route -> route.path("/api/v1/ratings/**")
                        .uri("lb://rating-service"))
                .route("ride-service", route -> route.path("/api/v1/rides/**")
                        .uri("lb://ride-service"))
                .build();
    }
}
