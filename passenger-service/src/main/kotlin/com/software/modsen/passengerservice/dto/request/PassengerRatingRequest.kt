package com.software.modsen.passengerservice.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Data for create passenger rating")
data class PassengerRatingRequest(
    val passengerId: Long,
    val passengerRating: Double
)