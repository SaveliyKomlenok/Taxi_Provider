package com.software.modsen.ratingservice.repository;

import com.software.modsen.ratingservice.entity.Rating;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Optional<Rating> findRatingByRideIdAndDriverIdAndPassengerId(Long rideId, Long driverId, Long passengerId);

    List<Rating> findRatingsByPassengerIdAndPassengerRatingNotNull(Long passengerId);

    List<Rating> findRatingsByDriverIdAndDriverRatingNotNull(Long driverId);
}