package com.software.modsen.passengerservice.component.rating

import com.software.modsen.passengerservice.entity.PassengerRating
import com.software.modsen.passengerservice.exception.PassengerRatingNotExistsException
import com.software.modsen.passengerservice.repository.PassengerRatingRepository
import com.software.modsen.passengerservice.service.PassengerService
import com.software.modsen.passengerservice.service.impl.PassengerRatingServiceImpl
import com.software.modsen.passengerservice.util.TestEntities
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.spring.CucumberContextConfiguration
import org.mockito.InjectMocks
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.mockito.ArgumentMatchers.any

import java.util.Optional

import com.software.modsen.passengerservice.util.ExceptionMessages.RATING_NOT_EXISTS
import com.software.modsen.passengerservice.util.TestEntities.PASSENGER_ID
import com.software.modsen.passengerservice.util.TestEntities.PASSENGER_RATING
import org.mockito.Mockito.mock

@CucumberContextConfiguration
class PassengerRatingComponentTest {

    private var passengerRatingRepository: PassengerRatingRepository = mock(PassengerRatingRepository::class.java)

    private var passengerService: PassengerService = mock(PassengerService::class.java)

    @InjectMocks
    private lateinit var passengerRatingService: PassengerRatingServiceImpl

    private lateinit var passengerRating: PassengerRating
    private var exception: Exception? = null

    @Given("A passenger rating with id {long} exists")
    fun aPassengerWithIdExists(id: Long) {
        passengerRating = TestEntities.getTestPassengerRating()
        `when`(passengerRatingRepository.findPassengerRatingByPassengerId(id)).thenReturn(passengerRating)
    }

    @When("I request the passenger rating with id {long} from getById method")
    fun iRequestThePassengerRatingWithIdFromGetByIdMethod(id: Long) {
        try {
            passengerRating = passengerRatingService.getByPassengerId(id)
        } catch (e: PassengerRatingNotExistsException) {
            exception = e
        }
    }

    @Then("I should receive the passenger rating details with id {long}")
    fun iShouldReceiveThePassengerRatingDetailsWithId(id: Long) {
        assertNotNull(passengerRating)
        assertEquals(id, passengerRating.passenger?.id)
    }

    @Given("A passenger rating exists with id {long} does not exist")
    fun aPassengerRatingExistsWithIdDoesNotExist(id: Long) {
        `when`(passengerRatingRepository.findById(id)).thenReturn(Optional.empty())
    }

    @Then("I should receive a PassengerRatingNotExistsException with id {long}")
    fun iShouldReceiveAPassengerRatingNotExistsExceptionWithId(id: Long) {
        val expected = String.format(RATING_NOT_EXISTS, id)
        val actual = exception?.message

        assertThat(actual).isEqualTo(expected)
    }

    @Given("A passenger rating with id {long} does not exists and saved")
    fun aPassengerRatingWithIdDoesNotExistsAndSaved(id: Long) {
        passengerRating = TestEntities.getTestPassengerRating()
        `when`(passengerRatingRepository.findById(id)).thenReturn(Optional.of(passengerRating))
        `when`(passengerRatingRepository.save(any(PassengerRating::class.java))).thenReturn(passengerRating)
    }

    @When("I save passenger rating")
    fun iSavePassengerRating() {
        passengerRating = passengerRatingRepository.save(passengerRating)
    }

    @Then("The new passenger rating should be saved successfully")
    fun theNewPassengerRatingShouldBeSavedSuccessfully() {
        val passenger = TestEntities.getTestPassenger()
        assertNotNull(passengerRating)
        assertEquals(passenger.email, passengerRating.passenger?.email)
        assertEquals(PASSENGER_RATING, passengerRating.rating)
        verify(passengerRatingRepository).save(any(PassengerRating::class.java))
    }

    @Given("A passenger rating with id {long} exists and saved")
    fun aPassengerRatingWithIdExistsAndSaved(id: Long) {
        passengerRating = TestEntities.getTestPassengerRating()
        `when`(passengerRatingRepository.findById(id)).thenReturn(Optional.of(passengerRating))
        `when`(passengerRatingRepository.save(passengerRating)).thenReturn(passengerRating)
    }

    @Then("The existing passenger rating should be saved successfully")
    fun theExistingPassengerRatingShouldBeSavedSuccessfully() {
        assertNotNull(passengerRating)
        assertEquals(PASSENGER_ID, passengerRating.passenger?.id)
        assertEquals(PASSENGER_RATING, passengerRating.rating)
        verify(passengerRatingRepository).save(passengerRating)
    }
}