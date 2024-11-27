package com.software.modsen.ratingservice.repository;

import com.software.modsen.ratingservice.entity.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {
    Optional<Rating> findRatingByRideIdAndDriverIdAndPassengerId(Long rideId, Long driverId, Long passengerId);

    List<Rating> findRatingsByPassengerIdAndPassengerRatingNotNull(Long passengerId);

    List<Rating> findRatingsByDriverIdAndDriverRatingNotNull(Long driverId);
}