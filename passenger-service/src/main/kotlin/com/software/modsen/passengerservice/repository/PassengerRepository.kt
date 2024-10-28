package com.software.modsen.passengerservice.repository

import com.software.modsen.passengerservice.entity.Passenger
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PassengerRepository : JpaRepository<Passenger, Long> {
    fun findAllByRestrictedIsTrue(pageRequest: PageRequest): List<Passenger>
    fun findPassengerByEmailAndPhoneNumber(email: String, phoneNumber: String): Passenger?
}