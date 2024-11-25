package com.software.modsen.driverservice.mapper;

import com.software.modsen.driverservice.dto.request.DriverCreateRequest;
import com.software.modsen.driverservice.dto.request.DriverUpdateRequest;
import com.software.modsen.driverservice.dto.response.CarResponse;
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
                .car(car)
                .firstname(request.getFirstname())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }

    public DriverResponse toResponse(Driver driver) {
        CarResponse car = null;
        if(driver.getCar() != null){
            car = CarResponse.builder()
                    .id(driver.getCar().getId())
                    .kind(driver.getCar().getKind())
                    .color(driver.getCar().getColor())
                    .number(driver.getCar().getNumber())
                    .tariff(driver.getCar().getTariff().toString())
                    .isRestricted(driver.getCar().isRestricted())
                    .build();
        }
        return DriverResponse.builder()
                .id(driver.getId())
                .car(car)
                .firstname(driver.getFirstname())
                .surname(driver.getSurname())
                .patronymic(driver.getPatronymic())
                .email(driver.getEmail())
                .phoneNumber(driver.getPhoneNumber())
                .gender(String.valueOf(driver.getGender()))
                .build();
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
