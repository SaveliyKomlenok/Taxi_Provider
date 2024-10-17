package com.software.modsen.passengerservice.component.passenger;

import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.exception.PassengerAlreadyExistsException;
import com.software.modsen.passengerservice.exception.PassengerNotExistsException;
import com.software.modsen.passengerservice.repository.PassengerRepository;
import com.software.modsen.passengerservice.service.impl.PassengerServiceImpl;
import com.software.modsen.passengerservice.util.TestEntities;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_ALREADY_EXISTS;
import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_NOT_EXISTS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@CucumberContextConfiguration
public class PassengerComponentTest {
    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    private Passenger passenger;
    private Exception exception;

    @Given("A passenger with id {long} exists")
    public void aPassengerWithIdExists(Long id) {
        passenger = TestEntities.getTestPassenger();
        when(passengerRepository.findById(id)).thenReturn(Optional.of(passenger));
    }

    @When("I request the passenger with id {long} from getById method")
    public void iRequestThePassengerWithId(Long id) {
        try {
            passenger = passengerService.getById(id);
        } catch (PassengerNotExistsException e) {
            exception = e;
        }
    }

    @Then("I should receive the passenger details with id {long}")
    public void iShouldReceiveThePassengerDetails(Long id) {
        assertNotNull(passenger);
        assertEquals(id, passenger.getId());
    }

    @Given("A passenger exists with id {long} does not exist")
    public void aPassengerExistsWithIdDoesNotExist(Long id) {
        when(passengerRepository.findById(id)).thenReturn(Optional.empty());
    }

    @Then("I should receive a PassengerNotExistsException with id {long}")
    public void iShouldReceiveAPassengerNotExistsExceptionWithId(Long id) {
        String expected = String.format(PASSENGER_NOT_EXISTS, id);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }


    @Given("A passenger with email {string} and phone number {string} does not exist")
    public void aPassengerWithEmailAndPhoneNumberDoesNotExist(String email, String phoneNumber) {
        passenger = TestEntities.getTestPassenger();
        when(passengerRepository.findPassengerByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(Optional.empty());
        when(passengerRepository.save(passenger)).thenReturn(passenger);
    }

    @When("I save the passenger")
    public void iSaveThePassenger() {
        try {
            passenger = passengerService.save(passenger);
        } catch (PassengerAlreadyExistsException e) {
            exception = e;
        }
    }

    @Then("The passenger should be saved successfully")
    public void thePassengerShouldBeSavedSuccessfully() {
        Passenger expected = TestEntities.getTestPassenger();
        assertEquals(passenger, expected);
        verify(passengerRepository).save(passenger);
    }

    @Given("A passenger with email {string} and phone number {string} already exists")
    public void aPassengerWithEmailAndPhoneNumberAlreadyExists(String email, String phoneNumber) {
        passenger = TestEntities.getTestPassenger();
        when(passengerRepository.findPassengerByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(Optional.of(passenger));
        when(passengerRepository.save(passenger)).thenReturn(passenger);
    }

    @Then("I should receive a PassengerAlreadyExistsException")
    public void iShouldReceiveAPassengerAlreadyExistsException() {
        String expected = String.format(PASSENGER_ALREADY_EXISTS);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A passenger with id {long} exists and saved")
    public void aPassengerWithIdExistsAndSaved(Long id) {
        passenger = TestEntities.getTestPassenger();
        when(passengerRepository.findById(id)).thenReturn(Optional.of(passenger));
        when(passengerRepository.save(passenger)).thenReturn(passenger);
    }

    @When("I update the passenger")
    public void iUpdateThePassenger() {
        try {
            passenger = passengerService.update(passenger);
        } catch (PassengerAlreadyExistsException e) {
            exception = e;
        }
    }

    @Then("The passenger should be updated successfully")
    public void thePassengerShouldBeUpdatedSuccessfully() {
        Passenger expected = TestEntities.getTestPassenger();
        assertEquals(passenger, expected);
        verify(passengerRepository).save(passenger);
    }

    @Given("A passenger with email {string} and phone number {string} already exists for update")
    public void aPassengerWithEmailAndPhoneNumberAlreadyExistsForUpdate(String email, String phoneNumber) {
        Passenger secondPassenger = TestEntities.getSecondTestPassenger();
        when(passengerRepository.findPassengerByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(Optional.of(secondPassenger));
        when(passengerRepository.save(passenger)).thenReturn(passenger);
    }

    @When("I change the restrictions status of the passenger with id {long}")
    public void iChangeTheRestrictionsStatusOfThePassenger(Long id) {
        try {
            passenger = passengerService.changeRestrictionsStatus(id);
        } catch (PassengerNotExistsException e) {
            exception = e;
        }
    }

    @Then("The passenger restriction status should be changed")
    public void thePassengerRestrictionsStatusShouldBeChanged() {
        assertNotNull(passenger);
        assertTrue(passenger.isRestricted());
        verify(passengerRepository).save(passenger);
    }
}
