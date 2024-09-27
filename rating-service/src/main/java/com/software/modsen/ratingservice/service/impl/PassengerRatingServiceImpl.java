package com.software.modsen.ratingservice.service.impl;

import com.software.modsen.ratingservice.client.PassengerClient;
import com.software.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.software.modsen.ratingservice.dto.response.PassengerRatingResponse;
import com.software.modsen.ratingservice.service.PassengerRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerRatingServiceImpl implements PassengerRatingService {
    private final PassengerClient passengerClient;

    @Override
    public PassengerRatingResponse savePassengerRating(PassengerRatingRequest request) {
        return passengerClient.savePassengerRating(request);
    }
}
