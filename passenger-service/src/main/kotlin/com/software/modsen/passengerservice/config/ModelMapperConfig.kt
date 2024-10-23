package com.software.modsen.passengerservice.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ModelMapperConfig {
    @Bean
    open fun modelMapperBean(): ModelMapper {
        return ModelMapper()
    }
}