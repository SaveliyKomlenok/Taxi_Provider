package com.software.modsen.rideservice.component;

import com.software.modsen.rideservice.dto.request.RideCancelRequest;
import com.software.modsen.rideservice.dto.request.RideFinishRequest;
import com.software.modsen.rideservice.dto.request.RideStatusChangeRequest;
import com.software.modsen.rideservice.dto.response.DriverResponse;
import com.software.modsen.rideservice.dto.response.PassengerResponse;
import com.software.modsen.rideservice.entity.Ride;
import com.software.modsen.rideservice.enumiration.Status;
import com.software.modsen.rideservice.exception.PassengerRestrictedException;
import com.software.modsen.rideservice.exception.RideAcceptException;
import com.software.modsen.rideservice.exception.RideCancelException;
import com.software.modsen.rideservice.exception.RideChangeStatusException;
import com.software.modsen.rideservice.exception.RideFinishException;
import com.software.modsen.rideservice.exception.RideNotExistsException;
import com.software.modsen.rideservice.repository.RideRepository;
import com.software.modsen.rideservice.service.DriverService;
import com.software.modsen.rideservice.service.PassengerService;
import com.software.modsen.rideservice.service.RatingService;
import com.software.modsen.rideservice.service.impl.RideServiceImpl;
import com.software.modsen.rideservice.util.RideTestEntities;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.software.modsen.rideservice.util.ExceptionMessages.PASSENGER_RESTRICTED;
import static com.software.modsen.rideservice.util.ExceptionMessages.RIDE_NOT_ACCEPTED;
import static com.software.modsen.rideservice.util.ExceptionMessages.RIDE_NOT_CANCELED;
import static com.software.modsen.rideservice.util.ExceptionMessages.RIDE_NOT_EXISTS;
import static com.software.modsen.rideservice.util.ExceptionMessages.RIDE_NOT_FINISHED;
import static com.software.modsen.rideservice.util.ExceptionMessages.RIDE_STATUS_NOT_CHANGED;
import static com.software.modsen.rideservice.util.RideTestEntities.DRIVER_ID;
import static com.software.modsen.rideservice.util.RideTestEntities.PASSENGER_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
public class RideComponentTest {
    @Mock
    private RideRepository rideRepository;

    @Mock
    private PassengerService passengerService;

    @Mock
    private DriverService driverService;

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RideServiceImpl rideService;

    private Ride ride;
    private PassengerResponse passenger;
    private DriverResponse driver;
    private RideCancelRequest cancelRequest;
    private RideFinishRequest finishRequest;
    private RideStatusChangeRequest statusChangeRequest;
    private Exception exception;

    @Given("A ride with id {long} exists")
    public void aRideWithIDExists(Long id) {
        ride = RideTestEntities.getTestRide();
        when(rideRepository.findById(id)).thenReturn(Optional.of(ride));
    }

    @When("I request the ride by id {long}")
    public void iRequestTheRideById(Long id) {
        try {
            rideService.getById(id);
        } catch (RideNotExistsException e) {
            exception = e;
        }
    }

    @Then("I should receive the ride details with id {long}")
    public void iShouldReceiveTheRideDetails(Long id) {
        assertNotNull(ride);
        assertEquals(id, ride.getPassengerId());
    }

    @Given("A ride with id {long} does not exist")
    public void aRideWithIdDoesNotExist(Long id) {
        when(rideRepository.findById(id)).thenReturn(Optional.empty());
    }

    @Then("I should receive a RideNotExistsException with id {long}")
    public void iShouldReceiveARideNotExistsException(Long id) {
        String expected = String.format(RIDE_NOT_EXISTS, id);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A passenger with id {long}")
    public void aPassengerWithId(Long id) {
        passenger = RideTestEntities.getTestPassenger();
        ride = RideTestEntities.getTestRide();
        when(passengerService.getPassengerById(id)).thenReturn(passenger);
        when(rideRepository.save(ride)).thenReturn(ride);
    }

    @When("I create a new ride with passenger id {long}")
    public void iCreateANewRide(Long id) {
        try {
            ride = rideService.create(id, ride);
        } catch (PassengerRestrictedException e) {
            exception = e;
        }
    }

    @Then("I should receive the created ride details")
    public void iShouldReceiveTheCreatedRideDetails() {
        assertNotNull(ride);
        assertEquals(passenger.getId(), ride.getPassengerId());
        verify(rideRepository).save(ride);
    }

    @Given("A restricted passenger with id {long}")
    public void aRestrictedPassengerWithId(Long id) {
        passenger = RideTestEntities.getTestRestrictedPassenger();
        ride = RideTestEntities.getTestRide();
        when(passengerService.getPassengerById(id)).thenReturn(passenger);
    }

    @Then("I should receive a PassengerRestrictedException with id {long}")
    public void iShouldReceiveAPassengerRestrictedException(Long id) {
        String expected = String.format(PASSENGER_RESTRICTED, id);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A ride is in created status")
    public void aRideIsInCreatedStatus() {
        statusChangeRequest = RideTestEntities.getTestRideStatusChangeRequest();
        ride = RideTestEntities.getTestRide();
        DriverResponse driverForAccept = RideTestEntities.getTestDriver();
        when(rideRepository.findRideByIdAndStatusEquals(statusChangeRequest.getRideId(), Status.CREATED))
                .thenReturn(Optional.of(ride));
        when(driverService.getDriverById(DRIVER_ID)).thenReturn(driverForAccept);
        when(rideRepository.save(ride)).thenReturn(ride);
    }

    @When("I accept the ride with driver id {long}")
    public void iAcceptTheRide(Long id) {
        try {
            ride = rideService.accept(id, statusChangeRequest);
        } catch (RideAcceptException e) {
            exception = e;
        }
    }

    @Then("I should receive the accepted ride details")
    public void iShouldReceiveTheAcceptedRideDetails() {
        assertNotNull(ride);
        assertEquals(ride.getStatus(), Status.ACCEPTED);
        verify(rideRepository).save(ride);
    }

    @Given("A ride is in accepted status")
    public void aRideIsInAcceptedStatus() {
        statusChangeRequest = RideTestEntities.getTestRideStatusChangeRequest();
        DriverResponse driverForAccept = RideTestEntities.getTestDriver();
        ride = RideTestEntities.getTestRide();
        ride.setStatus(Status.ACCEPTED);
        when(rideRepository.findRideByIdAndStatusEquals(statusChangeRequest.getRideId(), ride.getStatus()))
                .thenReturn(Optional.of(ride));
        when(driverService.getDriverById(ride.getDriverId())).thenReturn(driverForAccept);
    }

    @Then("I should receive a RideAcceptException with id {long}")
    public void iShouldReceiveARideAcceptException(Long id) {
        String expected = String.format(RIDE_NOT_ACCEPTED, id);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A ride is on the way to destination")
    public void aRideIsOnTheWayToDestination() {
        finishRequest = RideTestEntities.getTestRideFinishRequest();
        ride = RideTestEntities.getTestRide();
        ride.setStatus(Status.ON_WAY_TO_DESTINATION);
        when(rideRepository.findRideByIdAndDriverIdAndStatusEquals(
                finishRequest.getRideId(),
                DRIVER_ID,
                Status.ON_WAY_TO_DESTINATION))
                .thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);
    }

    @When("I finish the ride with driver id {long}")
    public void iFinishTheRideWithAValidDriverID(Long id) {
        try {
            ride = rideService.finish(id, finishRequest);
        } catch (RideFinishException e) {
            exception = e;
        }
    }

    @Then("I should receive the finished ride details")
    public void iShouldReceiveTheFinishedRideDetails() {
        assertNotNull(ride);
        //assertEquals(ride.getStatus(), Status.ON_WAY_TO_DESTINATION);
        verify(rideRepository).save(ride);
    }

    @Given("A ride is not found")
    public void aRideIsNotFound() {
        finishRequest = RideTestEntities.getTestRideFinishRequest();
        ride = RideTestEntities.getTestRide();
        when(rideRepository.findRideByIdAndDriverIdAndStatusEquals(
                finishRequest.getRideId(),
                DRIVER_ID,
                Status.ON_WAY_TO_DESTINATION))
                .thenReturn(Optional.empty());
    }

    @Then("I should receive a RideFinishException with id {long}")
    public void iShouldReceiveARideFinishException(Long id) {
        String expected = String.format(RIDE_NOT_FINISHED, id);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A ride is in created status for cancel")
    public void aRideIsInCreatedStatusForCancel() {
        cancelRequest = RideTestEntities.getTestRideCancelRequest();
        ride = RideTestEntities.getTestRide();
        ride.setStatus(Status.CREATED);
        when(rideRepository.findRideByIdAndPassengerIdAndStatusEqualsOrStatusEquals(
                cancelRequest.getRideId(),
                PASSENGER_ID,
                Status.CREATED,
                Status.ACCEPTED))
                .thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);
    }

    @When("I cancel the ride with passenger id {long}")
    public void iCancelTheRideWithAValidPassengerID(Long id) {
        try {
            ride = rideService.cancel(id, cancelRequest);
        } catch (RideCancelException e) {
            exception = e;
        }
    }

    @Then("I should receive the canceled ride details")
    public void iShouldReceiveTheCanceledRideDetails() {
        assertNotNull(ride);
        assertEquals(ride.getStatus(), Status.CANCELED);
        verify(rideRepository).save(ride);
    }

    @Given("A ride cannot be canceled")
    public void aRideCannotBeCanceled() {
        cancelRequest = RideTestEntities.getTestRideCancelRequest();
        ride = RideTestEntities.getTestRide();
        when(rideRepository.findRideByIdAndPassengerIdAndStatusEqualsOrStatusEquals(
                cancelRequest.getRideId(),
                PASSENGER_ID,
                Status.CREATED,
                Status.ACCEPTED))
                .thenReturn(Optional.empty());
    }

    @Then("I should receive a RideCancelException with id {long}")
    public void iShouldReceiveARideCancelException(Long id) {
        String expected = String.format(RIDE_NOT_CANCELED, id);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A ride is in accepted status for change")
    public void aRideIsInAcceptedStatusForChange() {
        statusChangeRequest = RideTestEntities.getTestRideStatusChangeRequest();
        ride = RideTestEntities.getTestRide();
        ride.setStatus(Status.ACCEPTED);
        when(rideRepository.findRideByIdAndDriverIdAndStatusEqualsOrStatusEquals(
                statusChangeRequest.getRideId(),
                DRIVER_ID,
                Status.ACCEPTED,
                Status.ON_WAY_FOR_PASSENGER))
                .thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);
    }

    @When("I change the ride status with driver id {long}")
    public void iChangeTheRideStatus(Long id) {
        try {
            ride = rideService.changeStatus(id, statusChangeRequest);
        } catch (RideChangeStatusException e) {
            exception = e;
        }
    }

    @Then("I should receive the ride with updated status")
    public void iShouldReceiveTheRideWithUpdatedStatus() {
        assertNotNull(ride);
        assertEquals(Status.ON_WAY_FOR_PASSENGER, ride.getStatus());
        verify(rideRepository).save(ride);
    }

    @Given("A ride cannot be changed")
    public void aRideCannotBeChanged() {
        statusChangeRequest = RideTestEntities.getTestRideStatusChangeRequest();
        ride = RideTestEntities.getTestRide();
        when(rideRepository.findRideByIdAndDriverIdAndStatusEqualsOrStatusEquals(
                statusChangeRequest.getRideId(),
                DRIVER_ID,
                Status.ACCEPTED,
                Status.ON_WAY_FOR_PASSENGER))
                .thenReturn(Optional.empty());
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);
    }

    @Then("I should receive a RideChangeStatusException with id {long}")
    public void iShouldReceiveARideChangeStatusException(Long id) {
        String expected = String.format(RIDE_STATUS_NOT_CHANGED, id);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }
}
