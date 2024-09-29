package com.software.modsen.ratingservice.repository;

import com.software.modsen.ratingservice.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findRatingByRideIdAndDriverIdAndPassengerId(Long rideId, Long driverId, Long passengerId);
    List<Rating> findRatingsByPassengerIdAndPassengerRatingNotNull(Long passengerId);
    List<Rating> findRatingsByDriverIdAndDriverRatingNotNull(Long driverId);
}
