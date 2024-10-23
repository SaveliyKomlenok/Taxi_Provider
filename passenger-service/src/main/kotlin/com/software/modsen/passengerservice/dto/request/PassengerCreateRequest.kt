package com.software.modsen.passengerservice.dto.request

import com.software.modsen.passengerservice.util.CustomValidatePatterns
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

@Schema(description = "Data for create passenger")
data class PassengerCreateRequest (
    @field:Schema(defaultValue = "firstname")
    @field:Pattern(regexp = CustomValidatePatterns.NAME_PATTERN, message = "Incorrect firstname")
    val firstname: String,

    @field:Schema(defaultValue = "surname")
    @field:Pattern(regexp = CustomValidatePatterns.NAME_PATTERN, message = "Incorrect surname")
    val surname: String,

    @field:Schema(defaultValue = "patronymic")
    @field:Pattern(regexp = CustomValidatePatterns.NAME_PATTERN, message = "Incorrect patronymic")
    val patronymic: String,

    @field:Schema(defaultValue = "email")
    @field:Email(message = "Incorrect email")
    val email: String,

    @field:Schema(defaultValue = "phone number")
    @field:Pattern(regexp = CustomValidatePatterns.PHONE_NUMBER_PATTERN, message = "Incorrect phone number")
    val phoneNumber: String
)