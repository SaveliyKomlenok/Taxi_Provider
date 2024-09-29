package com.software.modsen.rideservice.service.impl;

import com.software.modsen.rideservice.dto.request.RatingPassengerRequest;
import com.software.modsen.rideservice.dto.request.RideCancelRequest;
import com.software.modsen.rideservice.dto.request.RideFinishRequest;
import com.software.modsen.rideservice.dto.request.RideStatusChangeRequest;
import com.software.modsen.rideservice.dto.response.DriverResponse;
import com.software.modsen.rideservice.entity.Ride;
import com.software.modsen.rideservice.enumiration.Status;
import com.software.modsen.rideservice.exception.*;
import com.software.modsen.rideservice.repository.RideRepository;
import com.software.modsen.rideservice.service.DriverService;
import com.software.modsen.rideservice.service.PassengerService;
import com.software.modsen.rideservice.service.RatingService;
import com.software.modsen.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static com.software.modsen.rideservice.util.ExceptionMessages.DRIVER_BUSY;
import static com.software.modsen.rideservice.util.ExceptionMessages.DRIVER_RESTRICTED;
import static com.software.modsen.rideservice.util.ExceptionMessages.INVALID_RIDE_STATUS;
import static com.software.modsen.rideservice.util.ExceptionMessages.PASSENGER_RESTRICTED;
import static com.software.modsen.rideservice.util.ExceptionMessages.RIDE_NOT_ACCEPTED;
import static com.software.modsen.rideservice.util.ExceptionMessages.RIDE_NOT_CANCELED;
import static com.software.modsen.rideservice.util.ExceptionMessages.RIDE_NOT_EXISTS;
import static com.software.modsen.rideservice.util.ExceptionMessages.RIDE_NOT_FINISHED;
import static com.software.modsen.rideservice.util.ExceptionMessages.RIDE_STATUS_NOT_CHANGED;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
    private final RideRepository rideRepository;
    private final PassengerService passengerService;
    private final DriverService driverService;
    private final RatingService ratingService;

    @Override
    public Ride getById(Long id) {
        return getOrThrow(id);
    }

    @Override
    public List<Ride> getAll(Integer pageNumber, Integer pageSize, String sortBy) {
        return rideRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))).getContent();
    }

    @Override
    public List<Ride> getAllByPassengerId(Long passengerId) {
        return rideRepository.findRidesByPassengerIdAndStatusEquals(passengerId, Status.FINISHED);
    }

    @Override
    public List<Ride> getAllByDriverId(Long driverId) {
        return rideRepository.findRidesByDriverIdAndStatusEquals(driverId, Status.FINISHED);
    }

    @Override
    public Ride create(Ride ride) {
        checkPassengerRestrict(ride.getPassengerId());
        ride.setStatus(Status.CREATED);
        ride.setStartDateTime(LocalDateTime.now());
        ride.setPrice(BigDecimal.valueOf(Math.random()).setScale(2, RoundingMode.DOWN));
        return rideRepository.save(ride);
    }

    private void checkPassengerRestrict(Long passengerId) {
        if (passengerService.getPassengerById(passengerId).isRestricted()) {
            throw new PassengerRestrictedException(String.format(PASSENGER_RESTRICTED, passengerId));
        }
    }

    @Override
    public Ride accept(RideStatusChangeRequest request) {
        checkDriverBusyAndRestricted(request.getDriverId());
        Ride ride = rideRepository.findRideByIdAndStatusEquals(
                        request.getRideId(),
                        Status.CREATED)
                .orElseThrow(() -> new RideAcceptException(String.format(RIDE_NOT_ACCEPTED, request.getRideId())));

        ride.setDriverId(request.getDriverId());
        ride.setStatus(Status.ACCEPTED);

        driverService.changeBusyStatus(request.getDriverId());
        return rideRepository.save(ride);
    }

    private void checkDriverBusyAndRestricted(Long driverId) {
        DriverResponse driver = driverService.getDriverById(driverId);
        if (driver.isRestricted()) {
            throw new DriverRestrictedException(String.format(DRIVER_RESTRICTED, driverId));
        }
        if (driver.isBusy()) {
            throw new DriverBusyException(String.format(DRIVER_BUSY, driverId));
        }
    }

    @Override
    public Ride finish(RideFinishRequest request) {
        Ride ride = rideRepository.findRideByIdAndDriverIdAndStatusEquals(
                        request.getRideId(),
                        request.getDriverId(),
                        Status.ON_WAY_TO_DESTINATION)
                .orElseThrow(() -> new RideFinishException(String.format(RIDE_NOT_FINISHED, request.getRideId())));

        ride.setStatus(Status.FINISHED);
        ride.setEndDateTime(LocalDateTime.now());

        driverService.changeBusyStatus(request.getDriverId());
        requestRateByDriver(request, ride);
        return rideRepository.save(ride);
    }

    private void requestRateByDriver(RideFinishRequest request, Ride ride) {
        ratingService.rateByDriver(RatingPassengerRequest.builder()
                        .rideId(ride.getId())
                        .driverId(ride.getDriverId())
                        .passengerId(ride.getPassengerId())
                        .passengerRating(request.getPassengerRating())
                .build());
    }

    @Override
    public Ride cancel(RideCancelRequest request) {
        Ride ride = rideRepository.findRideByIdAndPassengerIdAndStatusEqualsOrStatusEquals(
                        request.getRideId(),
                        request.getPassengerId(),
                        Status.CREATED,
                        Status.ACCEPTED)
                .orElseThrow(() -> new RideCancelException(String.format(RIDE_NOT_CANCELED, request.getRideId())));

        if (ride.getStatus().equals(Status.ACCEPTED)) {
            driverService.changeBusyStatus(ride.getDriverId());
        }
        ride.setStatus(Status.CANCELED);
        return rideRepository.save(ride);
    }

    @Override
    public Ride changeStatus(RideStatusChangeRequest request) {
        Ride ride = rideRepository.findRideByIdAndDriverIdAndStatusEqualsOrStatusEquals(
                        request.getRideId(),
                        request.getDriverId(),
                        Status.ACCEPTED,
                        Status.ON_WAY_FOR_PASSENGER)
                .orElseThrow(() -> new RideChangeStatusException(String.format(RIDE_STATUS_NOT_CHANGED, request.getRideId())));
        switch (ride.getStatus()) {
            case ACCEPTED -> ride.setStatus(Status.ON_WAY_FOR_PASSENGER);
            case ON_WAY_FOR_PASSENGER -> ride.setStatus(Status.ON_WAY_TO_DESTINATION);
            default -> throw new RideChangeStatusException(INVALID_RIDE_STATUS);
        }
        return rideRepository.save(ride);
    }

    private Ride getOrThrow(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RideNotExistsException(String.format(RIDE_NOT_EXISTS, id)));
    }
}
