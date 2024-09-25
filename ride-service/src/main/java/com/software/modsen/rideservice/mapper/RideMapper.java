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

    public Ride fromResponseToEntity(RideCreateRequest request){
        return mapper.map(request, Ride.class);
    }

    public RideResponse fromEntityToResponse(Ride ride) {
        return mapper.map(ride, RideResponse.class);
    }

    public RideListResponse fromListEntityToListResponse(List<Ride> rideList) {
        return RideListResponse.builder()
                .rideResponseList(rideList.stream()
                        .map(ride -> mapper.map(ride, RideResponse.class))
                        .toList())
                .build();
    }
}
