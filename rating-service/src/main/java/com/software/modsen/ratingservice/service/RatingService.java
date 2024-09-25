package com.software.modsen.ratingservice.service;

import com.software.modsen.ratingservice.dto.response.DriverRatingResponse;
import com.software.modsen.ratingservice.dto.response.PassengerRatingResponse;
import com.software.modsen.ratingservice.dto.request.RatingDriverRequest;
import com.software.modsen.ratingservice.entity.Rating;

import java.util.List;

public interface RatingService {
    List<Rating> getAll(Integer pageNumber, Integer pageSize, String sortBy);
    Rating ratingPassenger(Rating rating);
    Rating ratingDriver(RatingDriverRequest request);
    PassengerRatingResponse calculatePassengerRating(Long passengerId);
    DriverRatingResponse calculateDriverRating(Long driverId);
}
