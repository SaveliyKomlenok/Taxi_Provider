package com.software.modsen.rideservice.service;

import com.software.modsen.rideservice.dto.request.RideCancelRequest;
import com.software.modsen.rideservice.dto.request.RideFinishRequest;
import com.software.modsen.rideservice.dto.request.RideStatusChangeRequest;
import com.software.modsen.rideservice.entity.Ride;

import java.util.List;

public interface RideService {
    Ride getById(Long id);
    List<Ride> getAll(Integer pageNumber, Integer pageSize, String sortBy);
    List<Ride> getAllByPassengerId(Long passengerId);
    List<Ride> getAllByDriverId(Long driverId);
    Ride create(Long passengerId, Ride ride);
    Ride accept(Long driverId, RideStatusChangeRequest request);
    Ride finish(Long driverId, RideFinishRequest request);
    Ride cancel(Long passengerId, RideCancelRequest request);
    Ride changeStatus(Long driverId, RideStatusChangeRequest request);
}
