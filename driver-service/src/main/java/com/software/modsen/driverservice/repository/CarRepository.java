package com.software.modsen.driverservice.repository;

import com.software.modsen.driverservice.entity.Car;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CarRepository extends R2dbcRepository<Car, Long> {
    Flux<Car> findAllByRestrictedIsTrue(Pageable pageable);

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Mono<Car> findCarByNumber(String number);
}
