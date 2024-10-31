package com.software.modsen.driverservice.repository;

import com.software.modsen.driverservice.entity.Driver;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findAllByRestrictedIsTrue(PageRequest pageRequest);

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Optional<Driver> findDriverByEmailAndPhoneNumber(String email, String phoneNumber);

    Optional<Driver> findDriverByCarId(Long id);
}
