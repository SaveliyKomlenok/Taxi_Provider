package com.software.modsen.passengerservice.mapper;

import com.software.modsen.passengerservice.dto.request.PassengerRatingRequest;
import com.software.modsen.passengerservice.dto.response.PassengerRatingResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.entity.PassengerRating;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassengerRatingMapper {
    private final ModelMapper mapper;

    public PassengerRating toEntity(PassengerRatingRequest request){
        Passenger passenger = Passenger.builder()
                .id(request.getPassengerId())
                .build();
        return PassengerRating.builder()
                .rating(request.getPassengerRating())
                .passenger(passenger)
                .build();
    }

    public PassengerRatingResponse toResponse(PassengerRating passengerRating) {
        PassengerResponse passengerResponse = mapper.map(passengerRating.getPassenger(), PassengerResponse.class);
        return PassengerRatingResponse.builder()
                .id(passengerRating.getId())
                .passenger(passengerResponse)
                .passengerRating(passengerRating.getRating())
                .build();
    }
}
