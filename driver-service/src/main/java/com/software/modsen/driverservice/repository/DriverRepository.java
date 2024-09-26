package com.software.modsen.driverservice.repository;

import com.software.modsen.driverservice.entity.Driver;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findAllByRestrictedIsTrue(PageRequest pageRequest);
    Optional<Driver> findDriverByEmailAndPhoneNumber(String email, String phoneNumber);
    Optional<Driver> findDriverByCarId(Long id);
}
