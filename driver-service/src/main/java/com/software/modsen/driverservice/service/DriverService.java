package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.dto.DriverCreateRequest;
import com.software.modsen.driverservice.dto.DriverResponse;
import com.software.modsen.driverservice.dto.DriverUpdateRequest;

import java.util.List;

public interface DriverService {
    DriverResponse getById(Long id);

    List<DriverResponse> getAll(Integer pageNumber, Integer pageSize, String sortBy);

    DriverResponse save(DriverCreateRequest request);

    DriverResponse update(DriverUpdateRequest request);

    DriverResponse changeRestrictionsStatus(Long id);

    void delete(Long id);
}
