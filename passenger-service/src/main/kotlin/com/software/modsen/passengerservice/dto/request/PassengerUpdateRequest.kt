package com.software.modsen.passengerservice.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import com.software.modsen.passengerservice.util.CustomValidatePatterns.NAME_PATTERN
import com.software.modsen.passengerservice.util.CustomValidatePatterns.PHONE_NUMBER_PATTERN

@Schema(description = "Data for update passenger")
data class PassengerUpdateRequest(
    @field:Pattern(regexp = NAME_PATTERN, message = "Incorrect firstname")
    @field:Schema(defaultValue = "firstname")
    val firstname: String,

    @field:Pattern(regexp = NAME_PATTERN, message = "Incorrect surname")
    @field:Schema(defaultValue = "surname")
    val surname: String,

    @field:Pattern(regexp = NAME_PATTERN, message = "Incorrect patronymic")
    @field:Schema(defaultValue = "patronymic")
    val patronymic: String,

    @field:Email(message = "Incorrect email")
    @field:Schema(defaultValue = "email")
    val email: String,

    @field:Pattern(regexp = PHONE_NUMBER_PATTERN, message = "Incorrect phone number")
    @field:Schema(defaultValue = "phone number")
    val phoneNumber: String
)