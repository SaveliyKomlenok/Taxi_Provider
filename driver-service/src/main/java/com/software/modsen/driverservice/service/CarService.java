package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.dto.request.CarChangeStatusRequest;
import com.software.modsen.driverservice.entity.Car;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CarService {
    Mono<Car> getById(Long id);

    Flux<Car> getAll(Integer pageNumber, Integer pageSize, String sortBy, Boolean includeRestricted);

    Mono<Car> save(Car car);

    Mono<Car> update(Car car);

    Mono<Car> changeRestrictionsStatus(CarChangeStatusRequest request);
}
