package com.software.modsen.rideservice.service;

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
import com.software.modsen.rideservice.service.impl.RideServiceImpl;
import com.software.modsen.rideservice.util.RideTestEntities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.software.modsen.rideservice.util.RideTestEntities.EXPECTED_LIST_SIZE;
import static com.software.modsen.rideservice.util.RideTestEntities.FIRST_INDEX;
import static com.software.modsen.rideservice.util.RideTestEntities.PAGE_NUMBER;
import static com.software.modsen.rideservice.util.RideTestEntities.PAGE_SIZE;
import static com.software.modsen.rideservice.util.RideTestEntities.PASSENGER_ID;
import static com.software.modsen.rideservice.util.RideTestEntities.RIDE_ID;
import static com.software.modsen.rideservice.util.RideTestEntities.SORT_BY_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RideServiceTest {
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ride = RideTestEntities.getTestRide();
        passenger = RideTestEntities.getTestPassenger();
        driver = RideTestEntities.getTestDriver();
    }

    @Test
    void getById_ShouldReturnRide_WhenExists() {
        when(rideRepository.findById(RIDE_ID)).thenReturn(Optional.of(ride));

        Ride result = rideService.getById(RIDE_ID);

        assertEquals(ride, result);
        verify(rideRepository).findById(RIDE_ID);
    }

    @Test
    void getById_ShouldThrowException_WhenNotExists() {
        when(rideRepository.findById(RIDE_ID)).thenReturn(Optional.empty());

        assertThrows(RideNotExistsException.class, () -> rideService.getById(RIDE_ID));
        verify(rideRepository).findById(RIDE_ID);
    }

    @Test
    void getAll_ShouldReturnAllRides() {
        when(rideRepository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(SORT_BY_ID))))
                .thenReturn(new PageImpl<>(Collections.singletonList(ride)));

        List<Ride> result = rideService.getAll(PAGE_NUMBER, PAGE_SIZE, SORT_BY_ID);

        assertEquals(EXPECTED_LIST_SIZE, result.size());
        assertEquals(ride, result.get(FIRST_INDEX));
        verify(rideRepository).findAll(any(Pageable.class));
    }

    @Test
    void create_ShouldReturnRide_WhenCreatedSuccessfully() {
        when(passengerService.getPassengerById(PASSENGER_ID)).thenReturn(passenger);
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        Ride result = rideService.create(ride);

        assertEquals(ride, result);
        verify(rideRepository).save(ride);
    }

    @Test
    void create_ShouldThrowException_WhenPassengerIsRestricted() {
        PassengerResponse restrictedPassenger = RideTestEntities.getTestRestrictedPassenger();
        when(passengerService.getPassengerById(PASSENGER_ID)).thenReturn(restrictedPassenger);

        assertThrows(PassengerRestrictedException.class, () -> rideService.create(ride));
        verify(rideRepository, never()).save(any(Ride.class));
    }

    @Test
    void accept_ShouldReturnRide_WhenAcceptedSuccessfully() {
        RideStatusChangeRequest request = RideTestEntities.getTestRideStatusChangeRequest();
        when(rideRepository.findRideByIdAndStatusEquals(request.getRideId(), Status.CREATED))
                .thenReturn(Optional.of(ride));
        when(driverService.getDriverById(request.getDriverId())).thenReturn(driver);
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        Ride result = rideService.accept(request);

        assertEquals(ride, result);
        verify(rideRepository).save(ride);
    }

    @Test
    void accept_ShouldThrowException_WhenRideIsNotInCreatedStatus() {
        RideStatusChangeRequest request = RideTestEntities.getTestRideStatusChangeRequest();
        ride.setStatus(Status.ACCEPTED);
        when(rideRepository.findRideByIdAndStatusEquals(request.getRideId(), ride.getStatus()))
                .thenReturn(Optional.of(ride));
        when(driverService.getDriverById(ride.getDriverId())).thenReturn(driver);

        assertThrows(RideAcceptException.class, () -> rideService.accept(request));
        verify(rideRepository, never()).save(any(Ride.class));
    }

    @Test
    void finish_ShouldReturnRide_WhenFinishedSuccessfully() {
        RideFinishRequest request = RideTestEntities.getTestRideFinishRequest();
        ride.setStatus(Status.ON_WAY_TO_DESTINATION);
        when(rideRepository.findRideByIdAndDriverIdAndStatusEquals(request.getRideId(), request.getDriverId(), Status.ON_WAY_TO_DESTINATION))
                .thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        Ride result = rideService.finish(request);

        assertEquals(ride, result);
        verify(rideRepository).save(ride);
    }

    @Test
    void finish_ShouldThrowException_WhenRideCannotBeFinished() {
        RideFinishRequest request = RideTestEntities.getTestRideFinishRequest();
        when(rideRepository.findRideByIdAndDriverIdAndStatusEquals(request.getRideId(), request.getDriverId(), Status.ON_WAY_TO_DESTINATION))
                .thenReturn(Optional.empty());

        assertThrows(RideFinishException.class, () -> rideService.finish(request));
        verify(rideRepository, never()).save(any(Ride.class));
    }

    @Test
    void cancel_ShouldReturnRide_WhenCanceledSuccessfully() {
        RideCancelRequest request = RideTestEntities.getTestRideCancelRequest();
        ride.setStatus(Status.CREATED);
        when(rideRepository.findRideByIdAndPassengerIdAndStatusEqualsOrStatusEquals(request.getRideId(), request.getPassengerId(), Status.CREATED, Status.ACCEPTED))
                .thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        Ride result = rideService.cancel(request);

        assertEquals(ride, result);
        verify(rideRepository).save(ride);
    }

    @Test
    void cancel_ShouldThrowException_WhenRideCannotBeCanceled() {
        RideCancelRequest request = RideTestEntities.getTestRideCancelRequest();
        when(rideRepository.findRideByIdAndPassengerIdAndStatusEqualsOrStatusEquals(request.getRideId(), request.getPassengerId(), Status.CREATED, Status.ACCEPTED))
                .thenReturn(Optional.empty());

        assertThrows(RideCancelException.class, () -> rideService.cancel(request));
        verify(rideRepository, never()).save(any(Ride.class));
    }

    @Test
    void changeStatus_ShouldChangeRideStatus_WhenStatusIsChangedSuccessfully() {
        RideStatusChangeRequest request = RideTestEntities.getTestRideStatusChangeRequest();
        ride.setStatus(Status.ACCEPTED);
        when(rideRepository.findRideByIdAndDriverIdAndStatusEqualsOrStatusEquals(request.getRideId(), request.getDriverId(), Status.ACCEPTED, Status.ON_WAY_FOR_PASSENGER))
                .thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        Ride result = rideService.changeStatus(request);

        assertEquals(Status.ON_WAY_FOR_PASSENGER, result.getStatus());
        verify(rideRepository).save(ride);
    }

    @Test
    void changeStatus_ShouldChangeToDestination_WhenCurrentStatusIsOnWayForPassenger() {
        RideStatusChangeRequest request = RideTestEntities.getTestRideStatusChangeRequest();
        ride.setStatus(Status.ON_WAY_FOR_PASSENGER);
        when(rideRepository.findRideByIdAndDriverIdAndStatusEqualsOrStatusEquals(request.getRideId(), request.getDriverId(), Status.ACCEPTED, Status.ON_WAY_FOR_PASSENGER))
                .thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(ride);

        Ride result = rideService.changeStatus(request);

        assertEquals(Status.ON_WAY_TO_DESTINATION, result.getStatus());
        verify(rideRepository).save(ride);
    }

    @Test
    void changeStatus_ShouldThrowException_WhenRideIsNotFound() {
        RideStatusChangeRequest request = RideTestEntities.getTestRideStatusChangeRequest();
        when(rideRepository.findRideByIdAndDriverIdAndStatusEqualsOrStatusEquals(request.getRideId(), request.getDriverId(), Status.ACCEPTED, Status.ON_WAY_FOR_PASSENGER))
                .thenReturn(Optional.empty());

        assertThrows(RideChangeStatusException.class, () -> rideService.changeStatus(request));
        verify(rideRepository, never()).save(any(Ride.class));
    }

    @Test
    void changeStatus_ShouldThrowException_WhenInvalidStatus() {
        RideStatusChangeRequest request = RideTestEntities.getTestRideStatusChangeRequest();
        ride.setStatus(Status.FINISHED);
        when(rideRepository.findRideByIdAndDriverIdAndStatusEqualsOrStatusEquals(request.getRideId(), request.getDriverId(), Status.ACCEPTED, Status.ON_WAY_FOR_PASSENGER))
                .thenReturn(Optional.of(ride));

        assertThrows(RideChangeStatusException.class, () -> rideService.changeStatus(request));
        verify(rideRepository, never()).save(any(Ride.class));
    }
}
