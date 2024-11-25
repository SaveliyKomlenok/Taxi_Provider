package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.entity.DriverRating;
import reactor.core.publisher.Mono;

public interface DriverRatingService {
    Mono<DriverRating> getByDriverId(Long driverId);
    Mono<DriverRating> save(DriverRating driverRating);
}
