package com.software.modsen.passengerservice.unit

import com.software.modsen.passengerservice.entity.Passenger
import com.software.modsen.passengerservice.exception.PassengerAlreadyExistsException
import com.software.modsen.passengerservice.exception.PassengerNotExistsException
import com.software.modsen.passengerservice.repository.PassengerRepository
import com.software.modsen.passengerservice.service.impl.PassengerServiceImpl
import com.software.modsen.passengerservice.util.TestEntities
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class PassengerUnitTest {

    private var passengerRepository: PassengerRepository = mock(PassengerRepository::class.java)

    @InjectMocks
    lateinit var passengerService: PassengerServiceImpl

    private lateinit var passenger: Passenger

    @BeforeEach
    fun setUp() {
        passenger = TestEntities.getTestPassenger()
    }

    @Test
    fun `getById should return passenger when exists`() {
        `when`(passengerRepository.findById(TestEntities.PASSENGER_ID)).thenReturn(Optional.of(passenger))

        val result = passengerService.getById(TestEntities.PASSENGER_ID)

        assertEquals(passenger, result)
        verify(passengerRepository).findById(TestEntities.PASSENGER_ID)
    }

    @Test
    fun `getById should throw exception when not exists`() {
        `when`(passengerRepository.findById(TestEntities.PASSENGER_ID)).thenReturn(Optional.empty())

        assertFailsWith<PassengerNotExistsException> {
            passengerService.getById(TestEntities.PASSENGER_ID)
        }
        verify(passengerRepository).findById(TestEntities.PASSENGER_ID)
    }

    @Test
    fun `getAll should return all passengers when includeRestricted is false`() {
        `when`(passengerRepository.findAll(PageRequest.of(TestEntities.PAGE_NUMBER, TestEntities.PAGE_SIZE, Sort.by(TestEntities.SORT_BY_ID))))
            .thenReturn(PageImpl(listOf(passenger)))

        val result = passengerService.getAll(TestEntities.PAGE_NUMBER, TestEntities.PAGE_SIZE, TestEntities.SORT_BY_ID, false)

        assertEquals(TestEntities.EXPECTED_LIST_SIZE, result.size)
        assertEquals(passenger, result[TestEntities.FIRST_INDEX])
        verify(passengerRepository).findAll(any(Pageable::class.java))
    }

    @Test
    fun `save should return passenger when saved successfully`() {
        `when`(passengerRepository.findPassengerByEmailAndPhoneNumber(TestEntities.PASSENGER_EMAIL, TestEntities.PASSENGER_PHONE_NUMBER))
            .thenReturn(null)
        `when`(passengerRepository.save(passenger)).thenReturn(passenger)

        val result = passengerService.save(passenger)

        assertEquals(passenger, result)
        verify(passengerRepository).save(passenger)
    }

    @Test
    fun `save should throw exception when passenger already exists`() {
        `when`(passengerRepository.findPassengerByEmailAndPhoneNumber(TestEntities.PASSENGER_EMAIL, TestEntities.PASSENGER_PHONE_NUMBER))
            .thenReturn(passenger)

        assertFailsWith<PassengerAlreadyExistsException> {
            passengerService.save(passenger)
        }
        verify(passengerRepository, never()).save(any())
    }

    @Test
    fun `update should return updated passenger when update is successful`() {
        `when`(passengerRepository.findById(TestEntities.PASSENGER_ID)).thenReturn(Optional.of(passenger))
        `when`(passengerRepository.findPassengerByEmailAndPhoneNumber(TestEntities.PASSENGER_EMAIL, TestEntities.PASSENGER_PHONE_NUMBER))
            .thenReturn(null)
        `when`(passengerRepository.save(passenger)).thenReturn(passenger)

        val result = passengerService.update(TestEntities.PASSENGER_ID, passenger)

        assertEquals(passenger, result)
        verify(passengerRepository).save(passenger)
    }

    @Test
    fun `update should throw exception when passenger already exists`() {
        val secondPassenger = TestEntities.getSecondTestPassenger()
        `when`(passengerRepository.findById(TestEntities.PASSENGER_ID)).thenReturn(Optional.of(passenger))
        `when`(passengerRepository.findPassengerByEmailAndPhoneNumber(TestEntities.PASSENGER_EMAIL, TestEntities.PASSENGER_PHONE_NUMBER))
            .thenReturn(secondPassenger)

        assertFailsWith<PassengerAlreadyExistsException> {
            passengerService.update(TestEntities.PASSENGER_ID, passenger)
        }
        verify(passengerRepository, never()).save(any())
    }

    @Test
    fun `changeRestrictionsStatus should change status when passenger exists`() {
        val changeStatus = TestEntities.getTestPassengerChangeStatusRequest()
        `when`(passengerRepository.findById(TestEntities.PASSENGER_ID)).thenReturn(Optional.of(passenger))
        `when`(passengerRepository.save(passenger)).thenReturn(passenger)

        val updatedPassenger = passengerService.changeRestrictionsStatus(changeStatus)

        assertTrue(updatedPassenger.restricted)
        verify(passengerRepository, times(TestEntities.WANTED_NUMBER_OF_INVOCATIONS)).save(passenger)
    }

    @Test
    fun `changeRestrictionsStatus should throw exception when passenger does not exist`() {
        val changeStatus = TestEntities.getTestPassengerChangeStatusRequest()
        `when`(passengerRepository.findById(TestEntities.PASSENGER_ID)).thenReturn(Optional.empty())

        assertFailsWith<PassengerNotExistsException> {
            passengerService.changeRestrictionsStatus(changeStatus)
        }
        verify(passengerRepository, never()).save(any())
    }
}