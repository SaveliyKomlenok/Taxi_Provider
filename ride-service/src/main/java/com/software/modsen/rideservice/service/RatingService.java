package com.software.modsen.rideservice.service;

import com.software.modsen.rideservice.dto.request.RatingPassengerRequest;
import com.software.modsen.rideservice.dto.response.RatingResponse;

public interface RatingService {
    RatingResponse rateByDriver(RatingPassengerRequest request);
}
