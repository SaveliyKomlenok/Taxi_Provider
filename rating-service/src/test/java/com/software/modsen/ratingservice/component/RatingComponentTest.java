package com.software.modsen.ratingservice.component;

import com.software.modsen.ratingservice.dto.request.RatingDriverRequest;
import com.software.modsen.ratingservice.entity.Rating;
import com.software.modsen.ratingservice.exception.DriverAlreadyHasRatingException;
import com.software.modsen.ratingservice.exception.RatingDriverException;
import com.software.modsen.ratingservice.repository.RatingRepository;
import com.software.modsen.ratingservice.service.DriverRatingService;
import com.software.modsen.ratingservice.service.PassengerRatingService;
import com.software.modsen.ratingservice.service.impl.RatingServiceImpl;
import com.software.modsen.ratingservice.util.RatingTestEntities;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.software.modsen.ratingservice.util.RatingTestEntities.PASSENGER_ID;
import static com.software.modsen.ratingservice.util.RatingTestEntities.PASSENGER_RATING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
public class RatingComponentTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private PassengerRatingService passengerRatingService;

    @Mock
    private DriverRatingService driverRatingService;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private Rating rating;
    private RatingDriverRequest driverRatingRequest;
    private Exception exception;

    @Given("I have a valid passenger rating")
    public void iHaveAValidPassengerRating() {
        rating = RatingTestEntities.getTestRating();
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
    }

    @When("I save the passenger rating")
    public void iSaveThePassengerRating() {
        rating = ratingService.ratingPassenger(rating);
    }

    @Then("I should receive the saved passenger rating")
    public void iShouldReceiveTheSavedPassengerRating() {
        assertNotNull(rating);
        assertEquals(PASSENGER_RATING, rating.getPassengerRating());
        verify(ratingRepository).save(any(Rating.class));
    }

    @Given("I have a valid driver rating request")
    public void iHaveAValidDriverRatingRequest() {
        driverRatingRequest = RatingTestEntities.getDriverRatingRequest();
        rating = RatingTestEntities.getTestRating();
        when(ratingRepository.findRatingByRideIdAndDriverIdAndPassengerId(
                driverRatingRequest.getRideId(),
                driverRatingRequest.getDriverId(),
                PASSENGER_ID))
                .thenReturn(Optional.of(rating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
    }

    @Given("the driver has not been rated yet")
    public void theDriverHasNotBeenRatedYet() {
        rating.setDriverRating(null);
    }

    @When("I save the driver rating")
    public void iSaveTheDriverRating() {
        try {
            rating = ratingService.ratingDriver(PASSENGER_ID, driverRatingRequest);
        } catch (DriverAlreadyHasRatingException | RatingDriverException e){
            exception = e;
        }
    }

    @Then("I should receive the updated driver rating")
    public void iShouldReceiveTheUpdatedDriverRating() {
        assertNotNull(rating);
        assertEquals(RatingTestEntities.DRIVER_RATING, rating.getDriverRating());
        verify(ratingRepository).save(any(Rating.class));
    }

    @Given("the driver has already been rated")
    public void theDriverHasAlreadyBeenRated() {
        rating.setDriverRating(RatingTestEntities.DRIVER_RATING);
    }

    @Then("I should receive a DriverAlreadyHasRatingException")
    public void iShouldReceiveADriverAlreadyHasRatingException() {
        assertThrows(DriverAlreadyHasRatingException.class, () -> ratingService.ratingDriver(PASSENGER_ID, driverRatingRequest));
        verify(ratingRepository, never()).save(any());
    }

    @Given("the rating does not exist")
    public void theRatingDoesNotExist() {
        when(ratingRepository.findRatingByRideIdAndDriverIdAndPassengerId(
                driverRatingRequest.getRideId(),
                driverRatingRequest.getDriverId(),
                PASSENGER_ID))
                .thenReturn(Optional.empty());
    }

    @Then("I should receive a RatingDriverException")
    public void iShouldReceiveARatingDriverException() {
        assertThrows(RatingDriverException.class, () -> ratingService.ratingDriver(PASSENGER_ID, driverRatingRequest));
        verify(ratingRepository, never()).save(any());
    }
}