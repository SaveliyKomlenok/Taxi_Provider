package com.software.modsen.rideservice.service.impl;

import com.software.modsen.rideservice.annotation.LogExecutionTime;
import com.software.modsen.rideservice.dto.request.DriverChangeStatusRequest;
import com.software.modsen.rideservice.dto.request.RatingPassengerRequest;
import com.software.modsen.rideservice.dto.request.RideCancelRequest;
import com.software.modsen.rideservice.dto.request.RideFinishRequest;
import com.software.modsen.rideservice.dto.request.RideStatusChangeRequest;
import com.software.modsen.rideservice.dto.response.DriverResponse;
import com.software.modsen.rideservice.entity.Ride;
import com.software.modsen.rideservice.enumiration.Status;
import com.software.modsen.rideservice.exception.DriverBusyException;
import com.software.modsen.rideservice.exception.DriverRestrictedException;
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
import com.software.modsen.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public List<Ride> getAllByPassengerId(Long passengerId) {
        return rideRepository.findRidesByPassengerIdAndStatusEquals(passengerId, Status.FINISHED);
    }

    @Transactional
    @Override
    public List<Ride> getAllByDriverId(Long driverId) {
        return rideRepository.findRidesByDriverIdAndStatusEquals(driverId, Status.FINISHED);
    }

    @Override
    @LogExecutionTime
    public Ride create(Long passengerId, Ride ride) {
        checkPassengerRestrict(passengerId);
        ride.setPassengerId(passengerId);
        ride.setStatus(Status.CREATED);
        ride.setStartDateTime(LocalDateTime.now());
        ride.setPrice(BigDecimal.valueOf(Math.random() * 10).setScale(2, RoundingMode.DOWN));
        return rideRepository.save(ride);
    }

    private void checkPassengerRestrict(Long passengerId) {
        if (passengerService.getPassengerById(passengerId).isRestricted()) {
            throw new PassengerRestrictedException(String.format(PASSENGER_RESTRICTED, passengerId));
        }
    }

    @Transactional
    @Override
    public Ride accept(Long driverId, RideStatusChangeRequest request) {
        checkDriverBusyAndRestricted(driverId);
        Ride ride = rideRepository.findRideByIdAndStatusEquals(
                        request.getRideId(),
                        Status.CREATED)
                .orElseThrow(() -> new RideAcceptException(String.format(RIDE_NOT_ACCEPTED, request.getRideId())));

        ride.setDriverId(driverId);
        ride.setStatus(Status.ACCEPTED);

        driverService.changeBusyStatus(DriverChangeStatusRequest.builder()
                .id(driverId)
                .status(true)
                .build());
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

    @Transactional
    @Override
    public Ride finish(Long driverId, RideFinishRequest request) {
        Ride ride = rideRepository.findRideByIdAndDriverIdAndStatusEquals(
                        request.getRideId(),
                        driverId,
                        Status.ON_WAY_TO_DESTINATION)
                .orElseThrow(() -> new RideFinishException(String.format(RIDE_NOT_FINISHED, request.getRideId())));

        ride.setStatus(Status.FINISHED);
        ride.setEndDateTime(LocalDateTime.now());

        driverService.changeBusyStatus(DriverChangeStatusRequest.builder()
                .id(driverId)
                .status(false)
                .build());
        requestRatedPassenger(driverId, request, ride);
        return rideRepository.save(ride);
    }

    private void requestRatedPassenger(Long driverId, RideFinishRequest request, Ride ride) {
        ratingService.ratedPassenger(RatingPassengerRequest.builder()
                .rideId(ride.getId())
                .driverId(driverId)
                .passengerId(ride.getPassengerId())
                .passengerRating(request.getPassengerRating())
                .build());
    }

    @Transactional
    @Override
    public Ride cancel(Long passengerId, RideCancelRequest request) {
        Ride ride = rideRepository.findRideByIdAndPassengerIdAndStatusEqualsOrStatusEquals(
                        request.getRideId(),
                        passengerId,
                        Status.CREATED,
                        Status.ACCEPTED)
                .orElseThrow(() -> new RideCancelException(String.format(RIDE_NOT_CANCELED, request.getRideId())));

        if (ride.getStatus().equals(Status.ACCEPTED)) {
            driverService.changeBusyStatus(DriverChangeStatusRequest.builder()
                    .id(ride.getDriverId())
                    .status(false)
                    .build());
        }
        ride.setStatus(Status.CANCELED);
        return rideRepository.save(ride);
    }

    @Transactional
    @Override
    public Ride changeStatus(Long driverId, RideStatusChangeRequest request) {
        Ride ride = rideRepository.findRideByIdAndDriverIdAndStatusEqualsOrStatusEquals(
                        request.getRideId(),
                        driverId,
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
