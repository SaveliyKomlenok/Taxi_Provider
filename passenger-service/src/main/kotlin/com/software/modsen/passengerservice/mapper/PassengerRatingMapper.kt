package com.software.modsen.passengerservice.mapper

import com.software.modsen.passengerservice.dto.request.PassengerRatingRequest
import com.software.modsen.passengerservice.dto.response.PassengerRatingResponse
import com.software.modsen.passengerservice.entity.Passenger
import com.software.modsen.passengerservice.entity.PassengerRating
import org.springframework.stereotype.Component

@Component
class PassengerRatingMapper(private val passengerMapper: PassengerMapper) {

    fun toEntity(request: PassengerRatingRequest): PassengerRating {
        return PassengerRating(
            id = null,
            rating = request.passengerRating,
            passenger = Passenger(request.passengerId)
        )
    }

    fun toResponse(passengerRating: PassengerRating): PassengerRatingResponse {
        return PassengerRatingResponse(
            id = passengerRating.id!!,
            passenger = passengerMapper.toResponse(passengerRating.passenger!!),
            passengerRating = passengerRating.rating
        )
    }
}