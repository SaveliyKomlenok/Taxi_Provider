package com.software.modsen.passengerservice.component.passenger

import com.software.modsen.passengerservice.entity.Passenger
import com.software.modsen.passengerservice.exception.PassengerAlreadyExistsException
import com.software.modsen.passengerservice.exception.PassengerNotExistsException
import com.software.modsen.passengerservice.repository.PassengerRepository
import com.software.modsen.passengerservice.service.impl.PassengerServiceImpl
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
import org.junit.jupiter.api.Assertions.assertTrue

import java.util.Optional

import com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_ALREADY_EXISTS
import com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_NOT_EXISTS
import org.mockito.Mockito.mock

@CucumberContextConfiguration
class PassengerComponentTest {

    private var passengerRepository: PassengerRepository = mock(PassengerRepository::class.java)

    @InjectMocks
    private lateinit var passengerService: PassengerServiceImpl

    private lateinit var passenger: Passenger
    private var exception: Exception? = null

    @Given("A passenger with id {long} exists")
    fun aPassengerWithIdExists(id: Long) {
        passenger = TestEntities.getTestPassenger()
        `when`(passengerRepository.findById(id)).thenReturn(Optional.of(passenger))
    }

    @When("I request the passenger with id {long} from getById method")
    fun iRequestThePassengerWithId(id: Long) {
        try {
            passenger = passengerService.getById(id)
        } catch (e: PassengerNotExistsException) {
            exception = e
        }
    }

    @Then("I should receive the passenger details with id {long}")
    fun iShouldReceiveThePassengerDetails(id: Long) {
        assertNotNull(passenger)
        assertEquals(id, passenger.id)
    }

    @Given("A passenger exists with id {long} does not exist")
    fun aPassengerExistsWithIdDoesNotExist(id: Long) {
        `when`(passengerRepository.findById(id)).thenReturn(Optional.empty())
    }

    @Then("I should receive a PassengerNotExistsException with id {long}")
    fun iShouldReceiveAPassengerNotExistsException(id: Long) {
        val expected = String.format(PASSENGER_NOT_EXISTS, id)
        val actual = exception?.message

        assertThat(actual).isEqualTo(expected)
    }

    @Given("A passenger with email {string} and phone number {string} does not exist")
    fun aPassengerWithEmailAndPhoneNumberDoesNotExist(email: String, phoneNumber: String) {
        passenger = TestEntities.getTestPassenger()
        `when`(passengerRepository.findPassengerByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(null)
        `when`(passengerRepository.save(passenger)).thenReturn(passenger)
    }

    @When("I save the passenger")
    fun iSaveThePassenger() {
        try {
            passenger = passengerService.save(passenger)
        } catch (e: PassengerAlreadyExistsException) {
            exception = e
        }
    }

    @Then("The passenger should be saved successfully")
    fun thePassengerShouldBeSavedSuccessfully() {
        val expected = TestEntities.getTestPassenger()
        assertEquals(passenger.email, expected.email)
        verify(passengerRepository).save(passenger)
    }

    @Given("A passenger with email {string} and phone number {string} already exists")
    fun aPassengerWithEmailAndPhoneNumberAlreadyExists(email: String, phoneNumber: String) {
        passenger = TestEntities.getTestPassenger()
        `when`(passengerRepository.findPassengerByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(passenger)
        `when`(passengerRepository.save(passenger)).thenReturn(passenger)
    }

    @Then("I should receive a PassengerAlreadyExistsException")
    fun iShouldReceiveAPassengerAlreadyExistsException() {
        val expected = String.format(PASSENGER_ALREADY_EXISTS)
        val actual = exception?.message

        assertThat(actual).isEqualTo(expected)
    }

    @Given("A passenger with id {long} exists and saved")
    fun aPassengerWithIdExistsAndSaved(id: Long) {
        passenger = TestEntities.getTestPassenger()
        `when`(passengerRepository.findById(id)).thenReturn(Optional.of(passenger))
        `when`(passengerRepository.save(passenger)).thenReturn(passenger)
    }

    @When("I update the passenger")
    fun iUpdateThePassenger() {
        try {
            passenger = passengerService.update(passenger)
        } catch (e: PassengerAlreadyExistsException) {
            exception = e
        }
    }

    @Then("The passenger should be updated successfully")
    fun thePassengerShouldBeUpdatedSuccessfully() {
        val expected = TestEntities.getTestPassenger()
        assertEquals(passenger.phoneNumber, expected.phoneNumber)
        verify(passengerRepository).save(passenger)
    }

    @Given("A passenger with email {string} and phone number {string} already exists for update")
    fun aPassengerWithEmailAndPhoneNumberAlreadyExistsForUpdate(email: String, phoneNumber: String) {
        val secondPassenger = TestEntities.getSecondTestPassenger()
        `when`(passengerRepository.findPassengerByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(secondPassenger)
        `when`(passengerRepository.save(passenger)).thenReturn(passenger)
    }

    @When("I change the restrictions status of the passenger with id {long}")
    fun iChangeTheRestrictionsStatusOfThePassenger(id: Long) {
        try {
            passenger = passengerService.changeRestrictionsStatus(TestEntities.getTestPassengerChangeStatusRequest(id))
        } catch (e: PassengerNotExistsException) {
            exception = e
        }
    }

    @Then("The passenger restriction status should be changed")
    fun thePassengerRestrictionsStatusShouldBeChanged() {
        assertNotNull(passenger)
        assertTrue(passenger.restricted)
        verify(passengerRepository).save(passenger)
    }
}