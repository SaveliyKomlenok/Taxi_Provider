package com.software.modsen.ratingservice.service.impl;

import com.software.modsen.ratingservice.dto.response.DriverRatingResponse;
import com.software.modsen.ratingservice.dto.response.PassengerRatingResponse;
import com.software.modsen.ratingservice.dto.request.RatingDriverRequest;
import com.software.modsen.ratingservice.entity.Rating;
import com.software.modsen.ratingservice.exception.NotFoundDriverRatingException;
import com.software.modsen.ratingservice.exception.NotFoundPassengerRatingException;
import com.software.modsen.ratingservice.exception.RatingDriverException;
import com.software.modsen.ratingservice.repository.RatingRepository;
import com.software.modsen.ratingservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.software.modsen.ratingservice.util.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;

    @Override
    public List<Rating> getAll(Integer pageNumber, Integer pageSize, String sortBy) {
        return ratingRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))).getContent();
    }

    @Override
    public Rating ratingPassenger(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public Rating ratingDriver(RatingDriverRequest request) {
        Rating rating = ratingRepository.findRatingByRideIdAndDriverIdAndPassengerId(
                request.getRideId(),
                request.getDriverId(),
                request.getPassengerId())
                .orElseThrow(() -> new RatingDriverException(String.format(DRIVER_NOT_RATED, request.getDriverId())));
        rating.setDriverRating(request.getDriverRating());
        rating.setComment(request.getComment());
        return ratingRepository.save(rating);
    }

    @Override
    public PassengerRatingResponse calculatePassengerRating(Long passengerId) {
        List<Rating> ratingList = ratingRepository.findRatingsByPassengerId(passengerId);
        if (ratingList.isEmpty()) {
            throw new NotFoundPassengerRatingException(String.format(NOT_FOUND_PASSENGER_RATING, passengerId));
        }
        double passengerRating = ratingList.stream()
                .mapToInt(Rating::getPassengerRating)
                .average()
                .orElse(0.0);
        return PassengerRatingResponse.builder()
                .passengerId(passengerId)
                .passengerRating(passengerRating)
                .build();
    }

    @Override
    public DriverRatingResponse calculateDriverRating(Long driverId) {
        List<Rating> ratingList = ratingRepository.findRatingsByDriverIdAndDriverRatingNotNull(driverId);
        if (ratingList.isEmpty()) {
            throw new NotFoundDriverRatingException(String.format(NOT_FOUND_DRIVER_RATING, driverId));
        }
        double driverRating = ratingList.stream()
                .mapToInt(Rating::getDriverRating)
                .average()
                .orElse(0.0);
        return DriverRatingResponse.builder()
                .driverId(driverId)
                .driverRating(BigDecimal.valueOf(driverRating).setScale(2, RoundingMode.HALF_UP).doubleValue())
                .build();
    }
}
