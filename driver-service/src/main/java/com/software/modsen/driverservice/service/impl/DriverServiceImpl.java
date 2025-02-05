package com.software.modsen.driverservice.service.impl;

import com.software.modsen.driverservice.dto.request.DriverChangeStatusRequest;
import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.entity.Driver;
import com.software.modsen.driverservice.exception.CarOccupiedException;
import com.software.modsen.driverservice.exception.DriverAlreadyExistsException;
import com.software.modsen.driverservice.exception.DriverNotExistsException;
import com.software.modsen.driverservice.repository.DriverRepository;
import com.software.modsen.driverservice.service.CarService;
import com.software.modsen.driverservice.service.DriverService;
import com.software.modsen.driverservice.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.software.modsen.driverservice.util.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {
    private final CarService carService;
    private final DriverRepository driverRepository;
    private final RedisService redisService;

    @Override
    public Driver getById(Long id) {
        String key = "driver:" + id;
        Driver driver = (Driver) redisService.getValue(key);
        if (driver == null) {
            driver = getOrThrow(id);
            redisService.setValue(key, driver, 10);
        }
        return driver;
    }

    @Override
    public List<Driver> getAll(Integer pageNumber, Integer pageSize, String sortBy, Boolean includeRestricted) {
        if (includeRestricted != null && includeRestricted) {
            log.info("All restricted drivers");
            return driverRepository.findAllByRestrictedIsTrue(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy)));
        } else {
            log.info("All drivers");
            return driverRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))).getContent();
        }
    }

    @Transactional
    @Override
    public Driver save(Driver driver) {
        if(driverRepository.findDriverByEmailAndPhoneNumber(
                driver.getEmail(),
                driver.getPhoneNumber()).isPresent()){
            throw new DriverAlreadyExistsException(DRIVER_ALREADY_EXISTS);
        }
        log.info("Saving driver");
        return driverRepository.save(driver);
    }

    @Transactional
    @Override
    public Driver update(Long id, Driver driver) {
        driver.setId(id);
        getOrThrow(driver.getId());
        Driver existingDriver = driverRepository.findDriverByEmailAndPhoneNumber(
                        driver.getEmail(),
                        driver.getPhoneNumber())
                .orElse(null);
        if (existingDriver != null && !existingDriver.getId().equals(driver.getId())) {
            throw new DriverAlreadyExistsException(DRIVER_ALREADY_EXISTS);
        }
        driver.setCar(checkCarOccupancyForUpdateDriver(driver.getCar().getId(), driver.getId()));
        log.info("Updating driver");
        redisService.evictValue("driver:" + driver.getId());
        return driverRepository.save(driver);
    }

    private Car checkCarOccupancyForUpdateDriver(Long id, Long driverId) {
        Car car = carService.getById(id);
        Driver existingDriver = driverRepository.findDriverByCarId(id)
                .orElse(null);
        if (existingDriver != null && !existingDriver.getId().equals(driverId)) {
            throw new CarOccupiedException(String.format(CAR_OCCUPIED, id));
        }
        log.info("Check car occupancy");
        return car;
    }

    @Override
    public Driver changeRestrictionsStatus(DriverChangeStatusRequest request) {
        Driver driver = getOrThrow(request.getId());
        driver.setRestricted(request.isStatus());
        log.info("Change driver restrict status");
        redisService.evictValue("driver:" + driver.getId());
        return driverRepository.save(driver);
    }

    @Override
    public Driver changeBusyStatus(DriverChangeStatusRequest request) {
        Driver driver = getOrThrow(request.getId());
        driver.setBusy(request.isStatus());
        log.info("Change driver busy status");
        redisService.evictValue("driver:" + driver.getId());
        return driverRepository.save(driver);
    }

    private Driver getOrThrow(Long id) {
        log.info("Driver with id " + id);
        return driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotExistsException(String.format(DRIVER_NOT_EXISTS, id)));
    }
}
