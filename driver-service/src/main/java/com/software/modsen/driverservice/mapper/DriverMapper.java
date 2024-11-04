package com.software.modsen.driverservice.mapper;

import com.software.modsen.driverservice.dto.request.DriverCreateRequest;
import com.software.modsen.driverservice.dto.request.DriverUpdateRequest;
import com.software.modsen.driverservice.dto.response.DriverListResponse;
import com.software.modsen.driverservice.dto.response.DriverResponse;
import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.entity.Driver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DriverMapper {
    private final ModelMapper mapper;

    public Driver toEntity(DriverCreateRequest request) {
        return Driver.builder()
                .firstname(request.getFirstname())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .build();
    }

    public Driver toEntity(DriverUpdateRequest request) {
        Car car = Car.builder()
                .id(request.getCar())
                .build();
        return Driver.builder()
                .id(request.getId())
                .car(car)
                .firstname(request.getFirstname())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .build();
    }

    public DriverResponse toResponse(Driver driver) {
        return mapper.map(driver, DriverResponse.class);
    }

    public DriverListResponse toListResponse(List<Driver> driverList) {
        List<DriverResponse> driverResponseList = driverList.stream()
                .map(driver -> mapper.map(driver, DriverResponse.class))
                .toList();
        return DriverListResponse.builder()
                .driverResponseList(driverResponseList)
                .build();
    }
}
