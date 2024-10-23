package com.software.modsen.passengerservice.service

import com.software.modsen.passengerservice.entity.PassengerRating

interface PassengerRatingService {
    fun getByPassengerId(passengerId: Long): PassengerRating
    fun save(passengerRating: PassengerRating): PassengerRating
}