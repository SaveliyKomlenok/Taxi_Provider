package com.software.modsen.rideservice.service.impl;

import com.software.modsen.rideservice.client.RatingClient;
import com.software.modsen.rideservice.dto.request.RatingPassengerRequest;
import com.software.modsen.rideservice.dto.response.RatingResponse;
import com.software.modsen.rideservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingClient ratingClient;

    @Override
    public RatingResponse rateByDriver(RatingPassengerRequest request) {
        return ratingClient.rateByDriver(request);
    }
}
