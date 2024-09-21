package com.software.modsen.driverservice.configuration;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Config {
    @Bean
    public ModelMapper modelMapperBean() {
        return new ModelMapper();
    }
}