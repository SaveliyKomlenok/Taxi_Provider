package com.software.modsen.passengerservice.config

import com.software.modsen.passengerservice.converter.JwtAuthConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
open class SecurityConfig (private val jwtAuthConverter: JwtAuthConverter) {
        @Bean
        open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
            return http.csrf { csrf -> csrf.disable()}
                .authorizeHttpRequests { authManager -> authManager.anyRequest().authenticated() }
                .oauth2ResourceServer { oauth ->
                    oauth.jwt { jwtConfigurer ->
                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthConverter)
                    }
                }
                .sessionManagement { sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                }
                .build()
        }
}