package com.software.modsen.driverservice.service.impl;

import com.software.modsen.driverservice.entity.DriverRating;
import com.software.modsen.driverservice.exception.DriverRatingNotExistsException;
import com.software.modsen.driverservice.repository.DriverRatingRepository;
import com.software.modsen.driverservice.service.DriverRatingService;
import com.software.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.software.modsen.driverservice.util.ExceptionMessages.RATING_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class DriverRatingServiceImpl implements DriverRatingService {
    private final DriverRatingRepository driverRatingRepository;
    private final DriverService driverService;

    @Override
    public Mono<DriverRating> getByDriverId(Long driverId) {
        return getOrThrow(driverId);
    }

    @Override
    public Mono<DriverRating> save(DriverRating driverRating) {
        return driverRatingRepository.findDriverRatingByDriverId(driverRating.getDriver().getId())
                .flatMap(existingRating -> {
                    existingRating.setRating(driverRating.getRating());
                    return driverRatingRepository.save(existingRating);
                })
                .switchIfEmpty(driverService.getById(driverRating.getDriver().getId())
                        .flatMap(driver -> {
                            DriverRating newPassengerRating = new DriverRating();
                            newPassengerRating.setDriverId(driver.getId());
                            newPassengerRating.setRating(driverRating.getRating());
                            return driverRatingRepository.save(newPassengerRating);
                        })
                        .switchIfEmpty(Mono.error(new DriverRatingNotExistsException(String.format(RATING_NOT_EXISTS, driverRating.getDriver().getId()))))
                ).flatMap(changedRatingDriver -> {
                    Long driverId = changedRatingDriver.getDriverId();
                    if (driverId != null) {
                        return driverService.getById(driverId)
                                .map(driver -> {
                                    changedRatingDriver.setDriver(driver);
                                    return changedRatingDriver;
                                });
                    } else {
                        return Mono.just(changedRatingDriver);
                    }
                });
    }

    private Mono<DriverRating> getOrThrow(Long driverId) {
        return driverRatingRepository.findDriverRatingByDriverId(driverId)
                .switchIfEmpty(Mono.error(new DriverRatingNotExistsException(String.format(RATING_NOT_EXISTS, driverId))))
                .flatMap(changedRatingDriver -> {
                    Long driverIdForMap = changedRatingDriver.getDriverId();
                    if (driverIdForMap != null) {
                        return driverService.getById(driverIdForMap)
                                .map(driver -> {
                                    changedRatingDriver.setDriver(driver);
                                    return changedRatingDriver;
                                });
                    } else {
                        return Mono.just(changedRatingDriver);
                    }
                });
    }
}