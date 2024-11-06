package com.software.modsen.passengerservice.mapper

import com.software.modsen.passengerservice.dto.request.PassengerCreateRequest
import com.software.modsen.passengerservice.dto.request.PassengerUpdateRequest
import com.software.modsen.passengerservice.dto.response.PassengerListResponse
import com.software.modsen.passengerservice.dto.response.PassengerResponse
import com.software.modsen.passengerservice.entity.Passenger
import org.springframework.stereotype.Component

@Component
class PassengerMapper {
    fun toEntity(request: PassengerCreateRequest): Passenger {
        return Passenger(
            id = null,
            firstname = request.firstname,
            surname = request.surname,
            patronymic = request.patronymic,
            email = request.email,
            phoneNumber = request.phoneNumber,
            restricted = false,
            passengerRating = null
        )
    }

    fun toEntity(request: PassengerUpdateRequest): Passenger {
        return Passenger(
            id = null,
            firstname = request.firstname,
            surname = request.surname,
            patronymic = request.patronymic,
            email = request.email,
            phoneNumber = request.phoneNumber,
            restricted = false,
            passengerRating = null
        )
    }

    fun toResponse(passenger: Passenger): PassengerResponse {
        return PassengerResponse(
            id = passenger.id!!,
            firstname = passenger.firstname,
            surname = passenger.surname,
            patronymic = passenger.patronymic,
            email = passenger.email,
            phoneNumber = passenger.phoneNumber,
            restricted = passenger.restricted
        )
    }

    fun toListResponse(passengers: List<Passenger>): PassengerListResponse {
        return PassengerListResponse(
            items = passengers.map { toResponse(it) }
        )
    }
}