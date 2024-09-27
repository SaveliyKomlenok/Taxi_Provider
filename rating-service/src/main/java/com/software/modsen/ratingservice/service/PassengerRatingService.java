package com.software.modsen.ratingservice.service;

import com.software.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.software.modsen.ratingservice.dto.response.PassengerRatingResponse;

public interface PassengerRatingService {
    PassengerRatingResponse savePassengerRating(PassengerRatingRequest request);
}
