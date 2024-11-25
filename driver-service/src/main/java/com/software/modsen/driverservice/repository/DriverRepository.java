package com.software.modsen.driverservice.repository;

import com.software.modsen.driverservice.entity.Driver;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DriverRepository extends R2dbcRepository<Driver, Long> {
    Flux<Driver> findAllByRestrictedIsTrue(PageRequest pageRequest);

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Mono<Driver> findDriverByEmailAndPhoneNumber(String email, String phoneNumber);

    Mono<Driver> findDriverByCarId(Long id);
}
