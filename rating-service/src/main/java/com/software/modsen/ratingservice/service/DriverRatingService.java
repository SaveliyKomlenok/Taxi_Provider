package com.software.modsen.ratingservice.service;

import com.software.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.software.modsen.ratingservice.dto.response.DriverRatingResponse;

public interface DriverRatingService {
    DriverRatingResponse saveDriverRating(DriverRatingRequest request);
}
