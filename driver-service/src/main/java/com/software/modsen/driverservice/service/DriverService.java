package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.dto.request.DriverChangeStatusRequest;
import com.software.modsen.driverservice.entity.Driver;

import java.util.List;

public interface DriverService {
    Driver getById(Long id);

    List<Driver> getAll(Integer pageNumber, Integer pageSize, String sortBy, Boolean includeRestricted);

    Driver save(Driver driver);

    Driver update(Long id, Driver driver);

    Driver changeRestrictionsStatus(DriverChangeStatusRequest request);

    Driver changeBusyStatus(DriverChangeStatusRequest request);
}
