package com.software.modsen.rideservice.service.impl;

import com.software.modsen.rideservice.dto.request.RideCancelRequest;
import com.software.modsen.rideservice.dto.request.RideStatusChangeRequest;
import com.software.modsen.rideservice.entity.Ride;
import com.software.modsen.rideservice.enumiration.Status;
import com.software.modsen.rideservice.exception.*;
import com.software.modsen.rideservice.repository.RideRepository;
import com.software.modsen.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static com.software.modsen.rideservice.util.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
    private final RideRepository rideRepository;

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
        ride.setStatus(Status.CREATED);
        ride.setStartDateTime(LocalDateTime.now());
        ride.setPrice(BigDecimal.valueOf(Math.random()).setScale(2, RoundingMode.DOWN));
        return rideRepository.save(ride);
    }

    @Override
    public Ride accept(RideStatusChangeRequest request) {
        Ride ride = rideRepository.findRideByIdAndStatusEquals(
                        request.getRideId(),
                        Status.CREATED)
                .orElseThrow(() -> new RideAcceptException(String.format(RIDE_NOT_ACCEPTED, request.getRideId())));
        ride.setDriverId(request.getDriverId());
        ride.setStatus(Status.ACCEPTED);
        return rideRepository.save(ride);
    }

    @Override
    public Ride finish(RideStatusChangeRequest request) {
        Ride ride = rideRepository.findRideByIdAndDriverIdAndStatusEquals(
                        request.getRideId(),
                        request.getDriverId(),
                        Status.ON_WAY_TO_DESTINATION)
                .orElseThrow(() -> new RideFinishException(String.format(RIDE_NOT_FINISHED, request.getRideId())));
        ride.setStatus(Status.FINISHED);
        ride.setEndDateTime(LocalDateTime.now());
        return rideRepository.save(ride);
    }

    @Override
    public Ride cancel(RideCancelRequest request) {
        Ride ride = rideRepository.findRideByIdAndPassengerIdAndStatusEqualsOrStatusEquals(
                        request.getRideId(),
                        request.getPassengerId(),
                        Status.CREATED,
                        Status.ACCEPTED)
                .orElseThrow(() -> new RideCancelException(String.format(RIDE_NOT_CANCELED, request.getRideId())));
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
                .orElseThrow(() -> new RideIsNotExistsException(String.format(RIDE_NOT_EXISTS, id)));
    }
}
