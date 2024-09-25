package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.dto.CarCreateRequest;
import com.software.modsen.driverservice.dto.CarResponse;
import com.software.modsen.driverservice.dto.CarUpdateRequest;

import java.util.List;

public interface CarService {
    CarResponse getById(Long id);

    List<CarResponse> getAll(Integer pageNumber, Integer pageSize, String sortBy);

    CarResponse save(CarCreateRequest request);

    CarResponse update(CarUpdateRequest request);

    CarResponse changeRestrictionsStatus(Long id);
}
