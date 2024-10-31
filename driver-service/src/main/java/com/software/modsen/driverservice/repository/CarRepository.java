package com.software.modsen.driverservice.repository;

import com.software.modsen.driverservice.entity.Car;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByRestrictedIsTrue(PageRequest pageRequest);

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Optional<Car> findCarByNumber(String number);
}
