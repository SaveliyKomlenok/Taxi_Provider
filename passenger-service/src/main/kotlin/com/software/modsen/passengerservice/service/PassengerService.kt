package com.software.modsen.passengerservice.service

import com.software.modsen.passengerservice.dto.request.PassengerChangeStatusRequest
import com.software.modsen.passengerservice.entity.Passenger

interface PassengerService {
    fun getById(id: Long): Passenger
    fun getAll(pageNumber: Int, pageSize: Int, sortBy: String, includeRestricted: Boolean): List<Passenger?>
    fun save(passenger: Passenger): Passenger
    fun update(passenger: Passenger): Passenger
    fun changeRestrictionsStatus(request: PassengerChangeStatusRequest): Passenger
}