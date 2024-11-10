package com.software.modsen.passengerservice.repository

import com.software.modsen.passengerservice.entity.PassengerRating
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository

@Repository
interface PassengerRatingRepository : JpaRepository<PassengerRating, Long> {
    @Lock(value = LockModeType.OPTIMISTIC)
    fun findPassengerRatingByPassengerId(passengerId: Long): PassengerRating?
}