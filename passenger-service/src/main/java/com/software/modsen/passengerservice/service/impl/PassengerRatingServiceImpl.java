package com.software.modsen.passengerservice.service.impl;

import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.entity.PassengerRating;
import com.software.modsen.passengerservice.exception.PassengerRatingNotExistsException;
import com.software.modsen.passengerservice.repository.PassengerRatingRepository;
import com.software.modsen.passengerservice.service.PassengerRatingService;
import com.software.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.software.modsen.passengerservice.util.ExceptionMessages.RATING_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class PassengerRatingServiceImpl implements PassengerRatingService {
    private final PassengerRatingRepository passengerRatingRepository;
    private final PassengerService passengerService;

    @Override
    public PassengerRating getByPassengerId(Long passengerId) {
        return getOrThrow(passengerId);
    }

    @Override
    public PassengerRating save(PassengerRating passengerRating) {
        PassengerRating newPassengerRating =
                passengerRatingRepository.findPassengerRatingByPassengerId(passengerRating.getPassenger().getId())
                .orElse(new PassengerRating());
        Passenger passenger = passengerService.getById(passengerRating.getPassenger().getId());
        newPassengerRating.setPassenger(passenger);
        newPassengerRating.setRating(passengerRating.getRating());
        return passengerRatingRepository.save(newPassengerRating);
    }

    private PassengerRating getOrThrow(Long passengerId) {
        return passengerRatingRepository.findPassengerRatingByPassengerId(passengerId)
                .orElseThrow(() -> new PassengerRatingNotExistsException(String.format(RATING_NOT_EXISTS, passengerId)));
    }
}
