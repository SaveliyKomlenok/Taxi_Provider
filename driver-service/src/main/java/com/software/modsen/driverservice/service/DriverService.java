package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.dto.request.DriverChangeStatusRequest;
import com.software.modsen.driverservice.entity.Driver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DriverService {
    Mono<Driver> getById(Long id);

    Flux<Driver> getAll(Integer pageNumber, Integer pageSize, String sortBy, Boolean includeRestricted);

    Mono<Driver> save(Driver driver);

    Mono<Driver> update(Long id, Driver driver);

    Mono<Driver> changeRestrictionsStatus(DriverChangeStatusRequest request);

    Mono<Driver> changeBusyStatus(DriverChangeStatusRequest request);
}
