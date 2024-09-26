package com.software.modsen.driverservice.repository;

import com.software.modsen.driverservice.entity.DriverRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRatingRepository extends JpaRepository<DriverRating, Long> {
    Optional<DriverRating> findDriverRatingByDriverId(Long driverId);
}
