package com.software.modsen.driverservice.mapper;

import com.software.modsen.driverservice.dto.request.CarCreateRequest;
import com.software.modsen.driverservice.dto.response.CarListResponse;
import com.software.modsen.driverservice.dto.response.CarResponse;
import com.software.modsen.driverservice.dto.request.CarUpdateRequest;
import com.software.modsen.driverservice.entity.Car;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CarMapper {
    private final ModelMapper mapper;

    public Car toEntity(CarCreateRequest request) {
        return mapper.map(request, Car.class);
    }

    public Car toEntity(CarUpdateRequest request) {
        return mapper.map(request, Car.class);
    }

    public CarResponse toResponse(Car car) {
        return mapper.map(car, CarResponse.class);
    }

    public CarListResponse toListResponse(List<Car> carList) {
        List<CarResponse> carResponseList = carList.stream()
                .map(car -> mapper.map(car, CarResponse.class))
                .toList();
        return CarListResponse.builder()
                .carResponseList(carResponseList)
                .build();
    }
}
