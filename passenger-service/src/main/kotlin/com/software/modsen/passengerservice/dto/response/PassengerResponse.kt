package com.software.modsen.passengerservice.dto.response

data class PassengerResponse (
    val id: Long,
    val firstname: String,
    val surname: String,
    val patronymic: String,
    val email: String,
    val phoneNumber: String,
    val restricted: Boolean,
)