package com.software.modsen.driverservice.repository;

import com.software.modsen.driverservice.entity.Car;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllByRestrictedIsTrue(PageRequest pageRequest);
    Optional<Car> findCarByNumber(String number);
}
