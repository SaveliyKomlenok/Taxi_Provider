package com.software.modsen.passengerservice.repository

import com.software.modsen.passengerservice.entity.PassengerRating
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PassengerRatingRepository : JpaRepository<PassengerRating, Long> {
    fun findPassengerRatingByPassengerId(passengerId: Long): PassengerRating?
}