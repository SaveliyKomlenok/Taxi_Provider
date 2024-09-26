package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.entity.DriverRating;

public interface DriverRatingService {
    DriverRating getByDriverId(Long driverId);
    DriverRating save(DriverRating driverRating);
}
