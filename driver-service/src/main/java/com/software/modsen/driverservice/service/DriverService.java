package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.entity.Driver;

import java.util.List;

public interface DriverService {
    Driver getById(Long id);

    List<Driver> getAll(Integer pageNumber, Integer pageSize, String sortBy, Boolean includeRestricted);

    Driver save(Driver request);

    Driver update(Driver request);

    Driver changeRestrictionsStatus(Long id);

    Driver changeBusyStatus(Long id);
}
