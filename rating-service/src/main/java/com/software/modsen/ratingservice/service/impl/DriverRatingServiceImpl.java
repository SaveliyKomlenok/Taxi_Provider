package com.software.modsen.ratingservice.service.impl;

import com.software.modsen.ratingservice.client.DriverClient;
import com.software.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.software.modsen.ratingservice.dto.response.DriverRatingResponse;
import com.software.modsen.ratingservice.service.DriverRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverRatingServiceImpl implements DriverRatingService {
    private final DriverClient driverClient;

    @Override
    public DriverRatingResponse saveDriverRating(DriverRatingRequest request) {
        return driverClient.saveDriverRating(request);
    }
}
