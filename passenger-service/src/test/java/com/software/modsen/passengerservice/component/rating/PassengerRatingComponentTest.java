package com.software.modsen.passengerservice.component.rating;

import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.entity.PassengerRating;
import com.software.modsen.passengerservice.exception.PassengerRatingNotExistsException;
import com.software.modsen.passengerservice.repository.PassengerRatingRepository;
import com.software.modsen.passengerservice.service.impl.PassengerRatingServiceImpl;
import com.software.modsen.passengerservice.util.TestEntities;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.software.modsen.passengerservice.util.ExceptionMessages.RATING_NOT_EXISTS;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_ID;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_RATING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
public class PassengerRatingComponentTest {
    @Mock
    private PassengerRatingRepository passengerRatingRepository;

    @InjectMocks
    private PassengerRatingServiceImpl passengerRatingService;

    private PassengerRating passengerRating;
    private Exception exception;

    @Given("A passenger rating with id {long} exists")
    public void aPassengerWithIdExists(Long id) {
        passengerRating = TestEntities.getTestPassengerRating();
        when(passengerRatingRepository.findPassengerRatingByPassengerId(id)).thenReturn(Optional.of(passengerRating));
    }

    @When("I request the passenger rating with id {long} from getById method")
    public void iRequestThePassengerRatingWithIdFromGetByIdMethod(Long id) {
        try {
            passengerRating = passengerRatingService.getByPassengerId(id);
        } catch (PassengerRatingNotExistsException e) {
            exception = e;
        }
    }

    @Then("I should receive the passenger rating details with id {long}")
    public void iShouldReceiveThePassengerRatingDetailsWithId(Long id) {
        assertNotNull(passengerRating);
        assertEquals(id, passengerRating.getPassenger().getId());
    }

    @Given("A passenger rating exists with id {long} does not exist")
    public void aPassengerRatingExistsWithIdDoesNotExist(Long id) {
        when(passengerRatingRepository.findById(id)).thenReturn(Optional.empty());
    }

    @Then("I should receive a PassengerRatingNotExistsException with id {long}")
    public void iShouldReceiveAPassengerRatingNotExistsExceptionWithId(Long id) {
        String expected = String.format(RATING_NOT_EXISTS, id);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A passenger rating with id {long} does not exists and saved")
    public void aPassengerRatingWithIdDoesNotExistsAndSaved(Long id) {
        passengerRating = TestEntities.getTestPassengerRating();
        when(passengerRatingRepository.findById(id)).thenReturn(Optional.of(passengerRating));
        when(passengerRatingRepository.save(any(PassengerRating.class))).thenReturn(passengerRating);
    }

    @When("I save passenger rating")
    public void iSavePassengerRating() {
        passengerRating = passengerRatingRepository.save(passengerRating);
    }

    @Then("The new passenger rating should be saved successfully")
    public void theNewPassengerRatingShouldBeSavedSuccessfully() {
        Passenger passenger = TestEntities.getTestPassenger();
        assertNotNull(passengerRating);
        assertEquals(passenger, passengerRating.getPassenger());
        assertEquals(PASSENGER_RATING, passengerRating.getRating());
        verify(passengerRatingRepository).save(any(PassengerRating.class));
    }

    @Given("A passenger rating with id {long} exists and saved")
    public void aPassengerRatingWithIdExistsAndSaved(Long id) {
        passengerRating = TestEntities.getTestPassengerRating();
        when(passengerRatingRepository.findById(id)).thenReturn(Optional.of(passengerRating));
        when(passengerRatingRepository.save(passengerRating)).thenReturn(passengerRating);
    }

    @Then("The existing passenger rating should be saved successfully")
    public void theExistingPassengerRatingShouldBeSavedSuccessfully() {
        assertNotNull(passengerRating);
        assertEquals(PASSENGER_ID, passengerRating.getPassenger().getId());
        assertEquals(PASSENGER_RATING, passengerRating.getRating());
        verify(passengerRatingRepository).save(passengerRating);
    }
}
