package com.software.modsen.driverservice.repository;

import com.software.modsen.driverservice.entity.DriverRating;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DriverRatingRepository extends R2dbcRepository<DriverRating, Long> {
    @Lock(value = LockModeType.OPTIMISTIC)
    Mono<DriverRating> findDriverRatingByDriverId(Long driverId);
}
