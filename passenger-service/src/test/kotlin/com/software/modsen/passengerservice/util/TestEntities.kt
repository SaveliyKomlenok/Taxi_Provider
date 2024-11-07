package com.software.modsen.passengerservice.util

import com.software.modsen.passengerservice.dto.request.PassengerChangeStatusRequest
import com.software.modsen.passengerservice.dto.request.PassengerCreateRequest
import com.software.modsen.passengerservice.dto.request.PassengerRatingRequest
import com.software.modsen.passengerservice.dto.request.PassengerUpdateRequest
import com.software.modsen.passengerservice.entity.Passenger
import com.software.modsen.passengerservice.entity.PassengerRating

object TestEntities {
    const val PASSENGER_ID: Long = 1L
    const val PASSENGER_FIRSTNAME: String = "Джон"
    const val PASSENGER_SURNAME: String = "Уильямс"
    const val PASSENGER_PATRONYMIC: String = "Джонсон"
    const val PASSENGER_EMAIL: String = "test@example.com"
    const val PASSENGER_PHONE_NUMBER: String = "+375331234567"
    const val SECOND_PASSENGER_EMAIL: String = "example@test.com"
    const val SECOND_PASSENGER_PHONE_NUMBER: String = "+375337654321"
    const val PAGE_NUMBER: Int = 0
    const val PAGE_SIZE: Int = 10
    const val SORT_BY_ID: String = "id"
    const val EXPECTED_LIST_SIZE: Int = 1
    const val EXPECTED_PASSENGER_LIST_SIZE: Int = 2
    const val FIRST_INDEX: Int = 0
    const val WANTED_NUMBER_OF_INVOCATIONS: Int = 1
    const val PASSENGER_RATING: Double = 4.55
    const val SECOND_PASSENGER_ID: Long = 2L
    const val PASSENGER_BASE_URL: String = "http://localhost:8081/api/v1/passengers"
    const val PASSENGER_RATING_BASE_URL: String = "http://localhost:8081/api/v1/passenger-ratings"

    fun getTestPassenger(): Passenger {
        return Passenger(
            id = PASSENGER_ID,
            firstname = PASSENGER_FIRSTNAME,
            surname = PASSENGER_SURNAME,
            patronymic = PASSENGER_PATRONYMIC,
            email = PASSENGER_EMAIL,
            phoneNumber = PASSENGER_PHONE_NUMBER,
            restricted = false,
            passengerRating = null
        )
    }

    fun getTestPassengerChangeStatusRequest(): PassengerChangeStatusRequest {
        return PassengerChangeStatusRequest(
            id = PASSENGER_ID,
            status = true
        )
    }

    fun getSecondTestPassenger(): Passenger {
        return Passenger(
            id = SECOND_PASSENGER_ID,
            firstname = PASSENGER_FIRSTNAME,
            surname = PASSENGER_SURNAME,
            patronymic = PASSENGER_PATRONYMIC,
            email = PASSENGER_EMAIL,
            phoneNumber = PASSENGER_PHONE_NUMBER,
            restricted = false,
            passengerRating = null
        )
    }

    fun getTestPassengerRating(): PassengerRating {
        return PassengerRating(
            id = null,
            passenger = getTestPassenger(),
            rating = PASSENGER_RATING
        )
    }

    fun getPassengerForIT(): Passenger {
        return Passenger(
            id = null,
            firstname = PASSENGER_FIRSTNAME,
            surname = PASSENGER_SURNAME,
            patronymic = PASSENGER_PATRONYMIC,
            email = PASSENGER_EMAIL,
            phoneNumber = PASSENGER_PHONE_NUMBER,
            restricted = false,
            passengerRating = null
        )
    }

    fun getSecondPassengerForIT(): Passenger {
        return Passenger(
            id = null,
            firstname = PASSENGER_FIRSTNAME,
            surname = PASSENGER_SURNAME,
            patronymic = PASSENGER_PATRONYMIC,
            email = SECOND_PASSENGER_EMAIL,
            phoneNumber = SECOND_PASSENGER_PHONE_NUMBER,
            restricted = false,
            passengerRating = null
        )
    }

    fun getPassengerCreateRequestForIT(): PassengerCreateRequest {
        return PassengerCreateRequest(
            firstname = PASSENGER_FIRSTNAME,
            surname = PASSENGER_SURNAME,
            patronymic = PASSENGER_PATRONYMIC,
            email = PASSENGER_EMAIL,
            phoneNumber = PASSENGER_PHONE_NUMBER
        )
    }

    fun getPassengerUpdateRequestForIT(id: Long?): PassengerUpdateRequest {
        return PassengerUpdateRequest(
            id = id,
            firstname = PASSENGER_FIRSTNAME,
            surname = PASSENGER_SURNAME,
            patronymic = PASSENGER_PATRONYMIC,
            email = SECOND_PASSENGER_EMAIL,
            phoneNumber = PASSENGER_PHONE_NUMBER
        )
    }

    fun getTestPassengerChangeStatusRequest(id: Long?): PassengerChangeStatusRequest {
        return PassengerChangeStatusRequest(
            id = id!!,
            status = true
        )
    }

    fun getPassengerRatingForIT(passenger: Passenger): PassengerRating {
        return PassengerRating(
            id = null,
            passenger = passenger,
            rating = PASSENGER_RATING
        )
    }

    fun getPassengerRatingRequestForIT(passengerId: Long?): PassengerRatingRequest {
        return PassengerRatingRequest(
            passengerId = passengerId!!,
            passengerRating = PASSENGER_RATING
        )
    }
}