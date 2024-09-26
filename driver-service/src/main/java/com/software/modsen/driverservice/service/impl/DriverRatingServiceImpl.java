package com.software.modsen.driverservice.service.impl;

import com.software.modsen.driverservice.entity.Driver;
import com.software.modsen.driverservice.entity.DriverRating;
import com.software.modsen.driverservice.exception.DriverRatingNotExistsException;
import com.software.modsen.driverservice.repository.DriverRatingRepository;
import com.software.modsen.driverservice.service.DriverRatingService;
import com.software.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.software.modsen.driverservice.util.ExceptionMessages.RATING_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class DriverRatingServiceImpl implements DriverRatingService {
    private final DriverRatingRepository driverRatingRepository;
    private final DriverService driverService;

    @Override
    public DriverRating getByDriverId(Long driverId) {
        return getOrThrow(driverId);
    }

    @Override
    public DriverRating save(DriverRating driverRating) {
        DriverRating newPassengerRating =
                driverRatingRepository.findDriverRatingByDriverId(driverRating.getDriver().getId())
                        .orElse(new DriverRating());
        Driver driver = driverService.getById(driverRating.getDriver().getId());
        newPassengerRating.setDriver(driver);
        newPassengerRating.setRating(driverRating.getRating());
        return driverRatingRepository.save(newPassengerRating);
    }

    private DriverRating getOrThrow(Long driverId) {
        return driverRatingRepository.findDriverRatingByDriverId(driverId)
                .orElseThrow(() -> new DriverRatingNotExistsException(String.format(RATING_NOT_EXISTS, driverId)));
    }
}
