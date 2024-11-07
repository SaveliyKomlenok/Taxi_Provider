package com.software.modsen.passengerservice.dto.request

data class PassengerChangeStatusRequest(
    val id: Long,
    val status: Boolean
)