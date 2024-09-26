package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.entity.Car;

import java.util.List;

public interface CarService {
    Car getById(Long id);

    List<Car> getAll(Integer pageNumber, Integer pageSize, String sortBy, Boolean includeRestricted);

    Car save(Car car);

    Car update(Car car);

    Car changeRestrictionsStatus(Long id);
}
