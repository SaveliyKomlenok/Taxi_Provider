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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.software.modsen.driverservice.util.ExceptionMessages.CAR_OCCUPIED;
import static com.software.modsen.driverservice.util.ExceptionMessages.DRIVER_ALREADY_EXISTS;
import static com.software.modsen.driverservice.util.ExceptionMessages.DRIVER_NOT_EXISTS;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {
    private final CarService carService;
    private final DriverRepository driverRepository;

    @Override
    public Mono<Driver> getById(Long id) {
        return getOrThrow(id);
    }

    @Override
    public Flux<Driver> getAll(Integer pageNumber, Integer pageSize, String sortBy, Boolean includeRestricted) {
        if (includeRestricted != null && includeRestricted) {
            log.info("All restricted drivers");
            return driverRepository.findAllByRestrictedIsTrue(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy)));
        } else {
            log.info("All drivers");
            return driverRepository.findAll();
        }
    }

    @Transactional
    @Override
    public Mono<Driver> save(Driver driver) {
        Mono<Driver> driverMono = driverRepository.findDriverByEmailAndPhoneNumber(driver.getEmail(), driver.getPhoneNumber())
                .flatMap(existingDriver -> Mono.error(new DriverAlreadyExistsException(DRIVER_ALREADY_EXISTS)))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Saving driver");
                    return driverRepository.save(driver);
                })).cast(Driver.class);
        return driverMono.flatMap(changedDriver -> {
            changedDriver.setCar(null);
            return Mono.just(changedDriver);
        });
    }

    @Transactional
    @Override
    public Mono<Driver> update(Long id, Driver driver) {
        driver.setId(id);
        Mono<Driver> driverMono = getOrThrow(driver.getId())
                .flatMap(existingDriver ->
                        driverRepository.findDriverByEmailAndPhoneNumber(driver.getEmail(), driver.getPhoneNumber())
                                .filter(existing -> !existing.getId().equals(driver.getId()))
                                .flatMap(existing -> Mono.error(new DriverAlreadyExistsException(DRIVER_ALREADY_EXISTS)))
                                .switchIfEmpty(checkCarOccupancyForUpdateDriver(driver.getCar().getId(), driver.getId()))
                )
                .doOnSuccess(car -> log.info("Updating driver"))
                .then(driverRepository.save(driver));
        return driverMono.flatMap(changedDriver -> {
            changedDriver.setCar(driver.getCar());
            return Mono.just(changedDriver);
        });
    }

    private Mono<Car> checkCarOccupancyForUpdateDriver(Long id, Long driverId) {
        return carService.getById(id)
                .flatMap(car ->
                        driverRepository.findDriverByCarId(id)
                                .map(existingDriver -> {
                                    if (!existingDriver.getId().equals(driverId)) {
                                        throw new CarOccupiedException(String.format(CAR_OCCUPIED, id));
                                    }
                                    return car;
                                })
                                .defaultIfEmpty(car)
                )
                .doOnSuccess(car -> log.info("Check car occupancy for car id: {}", id));
    }

    @Override
    public Mono<Driver> changeRestrictionsStatus(DriverChangeStatusRequest request) {
        return getOrThrow(request.getId())
                .doOnSuccess(driver -> {
                    driver.setRestricted(request.isStatus());
                    log.info("Change driver restrict status");
                })
                .flatMap(changedDriver -> {
                    Long carId = changedDriver.getCarId();
                    if (carId != null) {
                        return carService.getById(carId)
                                .map(car -> {
                                    changedDriver.setCar(car);
                                    return changedDriver;
                                });
                    } else {
                        return Mono.just(changedDriver);
                    }
                })
                .flatMap(driverRepository::save);
    }

    @Override
    public Mono<Driver> changeBusyStatus(DriverChangeStatusRequest request) {
        return getOrThrow(request.getId())
                .doOnSuccess(driver -> {
                    driver.setBusy(request.isStatus());
                    log.info("Change driver busy status");
                })
                .flatMap(changedDriver -> {
                    Long carId = changedDriver.getCarId();
                    if (carId != null) {
                        return carService.getById(carId)
                                .map(car -> {
                                    changedDriver.setCar(car);
                                    return changedDriver;
                                });
                    } else {
                        return Mono.just(changedDriver);
                    }
                })
                .flatMap(driverRepository::save);
    }

    private Mono<Driver> getOrThrow(Long id) {
        log.info("Driver with id " + id);
        return driverRepository.findById(id)
                .switchIfEmpty(Mono.error(new DriverNotExistsException(String.format(DRIVER_NOT_EXISTS, id))))
                .flatMap(changedDriver -> {
                    Long carId = changedDriver.getCarId();
                    if (carId != null) {
                        return carService.getById(carId)
                                .map(car -> {
                                    changedDriver.setCar(car);
                                    return changedDriver;
                                });
                    } else {
                        return Mono.just(changedDriver);
                    }
                });
    }
}