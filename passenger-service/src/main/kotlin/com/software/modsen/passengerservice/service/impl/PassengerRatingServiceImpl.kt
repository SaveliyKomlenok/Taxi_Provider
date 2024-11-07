package com.software.modsen.passengerservice.service.impl

import com.software.modsen.passengerservice.entity.PassengerRating
import com.software.modsen.passengerservice.exception.PassengerRatingNotExistsException
import com.software.modsen.passengerservice.repository.PassengerRatingRepository
import com.software.modsen.passengerservice.service.PassengerRatingService
import com.software.modsen.passengerservice.service.PassengerService
import com.software.modsen.passengerservice.util.ExceptionMessages
import org.springframework.stereotype.Service

@Service
class PassengerRatingServiceImpl(
    private val passengerRatingRepository: PassengerRatingRepository,
    private val passengerService: PassengerService
) : PassengerRatingService {

    override fun getByPassengerId(passengerId: Long): PassengerRating {
        return getOrThrow(passengerId)
    }

    override fun save(passengerRating: PassengerRating): PassengerRating {
        val tempPassengerRating =
            passengerRatingRepository.findPassengerRatingByPassengerId(passengerRating.passenger?.id!!)
        val passenger = passengerService.getById(passengerRating.passenger?.id!!)
        if (tempPassengerRating == null) {
            val newPassengerRating = PassengerRating(
                rating = passengerRating.rating,
                passenger = passenger
            )
            return passengerRatingRepository.save(newPassengerRating)
        }
        tempPassengerRating.passenger = passenger
        tempPassengerRating.rating = passengerRating.rating
        return passengerRatingRepository.save(tempPassengerRating)
    }

    private fun getOrThrow(passengerId: Long): PassengerRating {
        return passengerRatingRepository.findPassengerRatingByPassengerId(passengerId)
            ?: throw PassengerRatingNotExistsException(
                String.format(
                    ExceptionMessages.RATING_NOT_EXISTS,
                    passengerId
                )
            )
    }
}