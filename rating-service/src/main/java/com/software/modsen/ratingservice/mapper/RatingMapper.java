package com.software.modsen.ratingservice.mapper;

import com.software.modsen.ratingservice.dto.response.RatingListResponse;
import com.software.modsen.ratingservice.dto.request.RatingPassengerRequest;
import com.software.modsen.ratingservice.dto.response.RatingResponse;
import com.software.modsen.ratingservice.entity.Rating;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RatingMapper {
    private final ModelMapper mapper;

    public Rating toEntity(RatingPassengerRequest request) {
        return Rating.builder()
                .rideId(request.getRideId())
                .driverId(request.getDriverId())
                .passengerId(request.getPassengerId())
                .passengerRating(request.getPassengerRating())
                .build();
    }

    public RatingResponse toResponse(Rating rating) {
        return mapper.map(rating, RatingResponse.class);
    }

    public RatingListResponse toListResponse(List<Rating> ratingList) {
        List<RatingResponse> ratingResponseList = ratingList.stream()
                .map(rating -> mapper.map(rating, RatingResponse.class))
                .toList();
        return RatingListResponse.builder()
                .ratingResponseList(ratingResponseList)
                .build();
    }
}
