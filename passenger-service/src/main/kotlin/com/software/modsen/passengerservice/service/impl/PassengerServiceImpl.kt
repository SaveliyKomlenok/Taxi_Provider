package com.software.modsen.passengerservice.service.impl

import com.software.modsen.passengerservice.dto.request.PassengerChangeStatusRequest
import com.software.modsen.passengerservice.entity.Passenger
import com.software.modsen.passengerservice.exception.PassengerAlreadyExistsException
import com.software.modsen.passengerservice.exception.PassengerNotExistsException
import com.software.modsen.passengerservice.repository.PassengerRepository
import com.software.modsen.passengerservice.service.PassengerService
import com.software.modsen.passengerservice.util.ExceptionMessages
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class PassengerServiceImpl(private val passengerRepository: PassengerRepository) : PassengerService {
    override fun getById(id: Long): Passenger {
        return getOrThrow(id)
    }

    override fun getAll(pageNumber: Int, pageSize: Int, sortBy: String, includeRestricted: Boolean): List<Passenger> {
        return if (includeRestricted) {
            passengerRepository.findAllByRestrictedIsTrue(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy)))
        } else {
            passengerRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))).content
        }
    }

    @Transactional
    override fun save(passenger: Passenger): Passenger {
        if (passengerRepository.findPassengerByEmailAndPhoneNumber(
                passenger.email,
                passenger.phoneNumber) != null) {
            throw PassengerAlreadyExistsException(ExceptionMessages.PASSENGER_ALREADY_EXISTS)
        }
        return passengerRepository.save(passenger)
    }

    @Transactional
    override fun update(id: Long, passenger: Passenger): Passenger {
        getOrThrow(id)
        val existingPassenger = passengerRepository
            .findPassengerByEmailAndPhoneNumber(passenger.email, passenger.phoneNumber)
        if (existingPassenger != null && existingPassenger.id != passenger.id) {
            throw PassengerAlreadyExistsException(ExceptionMessages.PASSENGER_ALREADY_EXISTS)
        }
        return passengerRepository.save(passenger)
    }

    override fun changeRestrictionsStatus(request: PassengerChangeStatusRequest): Passenger {
        val passenger = getOrThrow(request.id)
        passenger.restricted = request.status
        return passengerRepository.save(passenger)
    }

    private fun getOrThrow(id: Long): Passenger {
        return passengerRepository.findById(id)
            .orElseThrow { PassengerNotExistsException(String.format(ExceptionMessages.PASSENGER_NOT_EXISTS, id)) }
    }
}