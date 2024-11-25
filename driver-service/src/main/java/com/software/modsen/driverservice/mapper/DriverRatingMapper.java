package com.software.modsen.driverservice.mapper;

import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.dto.response.DriverRatingResponse;
import com.software.modsen.driverservice.dto.response.DriverResponse;
import com.software.modsen.driverservice.entity.Driver;
import com.software.modsen.driverservice.entity.DriverRating;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverRatingMapper {
    private final DriverMapper driverMapper;

    public DriverRating toEntity(DriverRatingRequest request){
        Driver driver = Driver.builder()
                .id(request.getDriverId())
                .build();
        return DriverRating.builder()
                .rating(request.getDriverRating())
                .driver(driver)
                .build();
    }

    public DriverRatingResponse toResponse(DriverRating driverRating) {
        DriverResponse driverResponse = driverMapper.toResponse(driverRating.getDriver());
        return DriverRatingResponse.builder()
                .id(driverRating.getId())
                .driver(driverResponse)
                .driverRating(driverRating.getRating())
                .build();
    }
}
