package com.software.modsen.passengerservice.dto.response

data class PassengerRatingResponse (
    val id: Long,
    val passenger: PassengerResponse,
    val passengerRating: Double
)