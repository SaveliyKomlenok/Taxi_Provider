package com.software.modsen.ratingservice.service.impl;

import com.software.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.software.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.software.modsen.ratingservice.dto.request.RatingDriverRequest;
import com.software.modsen.ratingservice.entity.Rating;
import com.software.modsen.ratingservice.exception.DriverAlreadyHasRatingException;
import com.software.modsen.ratingservice.exception.RatingDriverException;
import com.software.modsen.ratingservice.repository.RatingRepository;
import com.software.modsen.ratingservice.service.DriverRatingService;
import com.software.modsen.ratingservice.service.PassengerRatingService;
import com.software.modsen.ratingservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.software.modsen.ratingservice.util.ExceptionMessages.DRIVER_HAS_RATING;
import static com.software.modsen.ratingservice.util.ExceptionMessages.DRIVER_NOT_RATED;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final PassengerRatingService passengerRatingService;
    private final DriverRatingService driverRatingService;

    @Override
    public List<Rating> getAll(Integer pageNumber, Integer pageSize, String sortBy) {
        return ratingRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))).getContent();
    }

    @Override
    public Rating ratingPassenger(Rating rating) {
        Rating newRating = ratingRepository.save(rating);
        requestSavePassengerRating(newRating);
        return newRating;
    }

    private void requestSavePassengerRating(Rating rating) {
        double passengerRating = calculatePassengerRating(rating.getPassengerId());
        passengerRatingService.savePassengerRating(PassengerRatingRequest.builder()
                .passengerId(rating.getPassengerId())
                .passengerRating(passengerRating)
                .build());
    }

    @Override
    public Rating ratingDriver(RatingDriverRequest request) {
        Rating rating = ratingRepository.findRatingByRideIdAndDriverIdAndPassengerId(
                        request.getRideId(),
                        request.getDriverId(),
                        request.getPassengerId())
                .orElseThrow(() -> new RatingDriverException(String.format(DRIVER_NOT_RATED, request.getDriverId())));
        if (rating.getDriverRating() != null) {
            throw new DriverAlreadyHasRatingException(String.format(DRIVER_HAS_RATING, rating.getDriverId()));
        }
        rating.setDriverRating(request.getDriverRating());
        rating.setComment(request.getComment());
        Rating newRating = ratingRepository.save(rating);
        requestSaveDriverRating(newRating);
        return newRating;
    }

    private void requestSaveDriverRating(Rating rating) {
        double driverRating = calculateDriverRating(rating.getDriverId());
        driverRatingService.saveDriverRating(DriverRatingRequest.builder()
                .driverId(rating.getDriverId())
                .driverRating(driverRating)
                .build());
    }

    public double calculatePassengerRating(Long passengerId) {
        List<Rating> ratingList = ratingRepository.findRatingsByPassengerIdAndPassengerRatingNotNull(passengerId);
        return ratingList.stream()
                .mapToInt(Rating::getPassengerRating)
                .average()
                .orElse(0.0);
    }

    public double calculateDriverRating(Long driverId) {
        List<Rating> ratingList = ratingRepository.findRatingsByDriverIdAndDriverRatingNotNull(driverId);
        return ratingList.stream()
                .mapToInt(Rating::getDriverRating)
                .average()
                .orElse(0.0);
    }
}
