package com.software.modsen.passengerservice.unit

import com.software.modsen.passengerservice.entity.Passenger
import com.software.modsen.passengerservice.entity.PassengerRating
import com.software.modsen.passengerservice.exception.PassengerRatingNotExistsException
import com.software.modsen.passengerservice.repository.PassengerRatingRepository
import com.software.modsen.passengerservice.service.PassengerService
import com.software.modsen.passengerservice.service.impl.PassengerRatingServiceImpl
import com.software.modsen.passengerservice.util.ExceptionMessages
import com.software.modsen.passengerservice.util.TestEntities
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

@ExtendWith(MockitoExtension::class)
class PassengerRatingUnitTest {

    private var passengerRatingRepository: PassengerRatingRepository = mock(PassengerRatingRepository::class.java)

    private var passengerService: PassengerService = mock(PassengerService::class.java)

    @InjectMocks
    lateinit var passengerRatingService: PassengerRatingServiceImpl

    private lateinit var passenger: Passenger
    private lateinit var passengerRating: PassengerRating

    @BeforeEach
    fun setUp() {
        passenger = TestEntities.getTestPassenger()
        passengerRating = TestEntities.getTestPassengerRating()
    }

    @Test
    fun `getByPassengerId should return rating when exists`() {
        `when`(passengerRatingRepository.findPassengerRatingByPassengerId(TestEntities.PASSENGER_ID))
            .thenReturn(passengerRating)

        val result = passengerRatingService.getByPassengerId(TestEntities.PASSENGER_ID)

        assertNotNull(result)
        assertEquals(passenger.id, result.passenger?.id)
    }

    @Test
    fun `getByPassengerId should throw exception when not exists`() {
        `when`(passengerRatingRepository.findPassengerRatingByPassengerId(TestEntities.PASSENGER_ID))
            .thenReturn(null)

        val exception = assertFailsWith<PassengerRatingNotExistsException> {
            passengerRatingService.getByPassengerId(TestEntities.PASSENGER_ID)
        }

        val expectedMessage = String.format(ExceptionMessages.RATING_NOT_EXISTS, TestEntities.PASSENGER_ID)
        assertTrue(exception.message!!.contains(expectedMessage))
    }

    @Test
    fun `save should return new passenger rating when saved successfully`() {
        `when`(passengerRatingRepository.findPassengerRatingByPassengerId(TestEntities.PASSENGER_ID))
            .thenReturn(null)
        `when`(passengerService.getById(TestEntities.PASSENGER_ID)).thenReturn(passenger)
        `when`(passengerRatingRepository.save(any(PassengerRating::class.java))).thenReturn(passengerRating)

        val savedRating = passengerRatingService.save(passengerRating)

        assertNotNull(savedRating)
        assertEquals(TestEntities.PASSENGER_RATING, savedRating.rating)
        verify(passengerRatingRepository).save(any(PassengerRating::class.java))
    }

    @Test
    fun `save should return existing passenger rating when already exists`() {
        `when`(passengerRatingRepository.findPassengerRatingByPassengerId(TestEntities.PASSENGER_ID))
            .thenReturn(passengerRating)
        `when`(passengerService.getById(TestEntities.PASSENGER_ID)).thenReturn(passenger)
        `when`(passengerRatingRepository.save(passengerRating)).thenReturn(passengerRating)

        val savedRating = passengerRatingService.save(passengerRating)

        assertNotNull(savedRating)
        assertEquals(TestEntities.PASSENGER_ID, savedRating.passenger?.id)
        assertEquals(TestEntities.PASSENGER_RATING, savedRating.rating)
        verify(passengerRatingRepository).save(passengerRating)
    }
}