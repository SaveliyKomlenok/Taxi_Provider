package com.software.modsen.rideservice.mapper;

import com.software.modsen.rideservice.dto.request.RideCreateRequest;
import com.software.modsen.rideservice.dto.response.RideListResponse;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.entity.Ride;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RideMapper {
    private final ModelMapper mapper;

    public Ride toEntity(RideCreateRequest request){
        return Ride.builder()
                .addressFrom(request.getAddressFrom())
                .addressTo(request.getAddressTo())
                .build();
    }

    public RideResponse toResponse(Ride ride) {
        return mapper.map(ride, RideResponse.class);
    }

    public RideListResponse toListResponse(List<Ride> rideList) {
        return RideListResponse.builder()
                .rideResponseList(rideList.stream()
                        .map(ride -> mapper.map(ride, RideResponse.class))
                        .toList())
                .build();
    }
}
