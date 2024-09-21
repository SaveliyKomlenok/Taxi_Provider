package com.software.modsen.driverservice.repository;

import com.software.modsen.driverservice.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

}
