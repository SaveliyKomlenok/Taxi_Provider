package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.dto.request.PassengerRatingRequest;
import com.software.modsen.passengerservice.entity.PassengerRating;

public interface PassengerRatingService {
    PassengerRating getByPassengerId(Long passengerId);
    PassengerRating save(PassengerRatingRequest request);
}
