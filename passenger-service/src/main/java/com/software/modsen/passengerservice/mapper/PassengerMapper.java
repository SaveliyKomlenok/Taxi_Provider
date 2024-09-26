package com.software.modsen.passengerservice.mapper;

import com.software.modsen.passengerservice.dto.request.PassengerCreateRequest;
import com.software.modsen.passengerservice.dto.response.PassengerListResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.dto.request.PassengerUpdateRequest;
import com.software.modsen.passengerservice.entity.Passenger;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PassengerMapper {
    private final ModelMapper mapper;

    public Passenger toEntity(PassengerCreateRequest request) {
        return mapper.map(request, Passenger.class);
    }

    public Passenger toEntity(PassengerUpdateRequest request) {
        return mapper.map(request, Passenger.class);
    }

    public PassengerResponse toResponse(Passenger passenger) {
        return mapper.map(passenger, PassengerResponse.class);
    }

    public PassengerListResponse toListResponse(List<Passenger> rideList) {
        List<PassengerResponse> passengerResponseList = rideList.stream()
                .map(ride -> mapper.map(ride, PassengerResponse.class))
                .toList();
        return PassengerListResponse.builder()
                .passengerResponseList(passengerResponseList)
                .build();
    }
}
