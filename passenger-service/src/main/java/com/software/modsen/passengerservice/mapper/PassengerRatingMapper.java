package com.software.modsen.passengerservice.mapper;

import com.software.modsen.passengerservice.dto.response.PassengerRatingResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.entity.PassengerRating;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassengerRatingMapper {
    private final ModelMapper mapper;

    public PassengerRatingResponse fromEntityToResponse(PassengerRating passengerRating) {
        return PassengerRatingResponse.builder()
                .id(passengerRating.getId())
                .passenger(mapper.map(passengerRating.getPassenger(), PassengerResponse.class))
                .passengerRating(passengerRating.getRating())
                .build();
    }
}
