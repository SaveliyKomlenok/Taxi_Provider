package com.software.modsen.ratingservice.service;

import com.software.modsen.ratingservice.dto.request.RatingDriverRequest;
import com.software.modsen.ratingservice.entity.Rating;

import java.util.List;

public interface RatingService {
    List<Rating> getAll(Integer pageNumber, Integer pageSize, String sortBy);
    Rating ratingPassenger(Rating rating);
    Rating ratingDriver(Long passengerId, RatingDriverRequest request);
}
