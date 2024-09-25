package com.software.modsen.passengerservice.service.impl;

import com.software.modsen.passengerservice.dto.request.PassengerRatingRequest;
import com.software.modsen.passengerservice.entity.PassengerRating;
import com.software.modsen.passengerservice.exception.PassengerRatingIsNotExistsException;
import com.software.modsen.passengerservice.repository.PassengerRatingRepository;
import com.software.modsen.passengerservice.service.PassengerRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.software.modsen.passengerservice.util.ExceptionMessages.RATING_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class PassengerRatingServiceImpl implements PassengerRatingService {
    private final PassengerRatingRepository passengerRatingRepository;
    private final PassengerServiceImpl passengerService;

    @Override
    public PassengerRating getByPassengerId(Long passengerId) {
        return getOrThrow(passengerId);
    }

    @Override
    public PassengerRating save(PassengerRatingRequest request) {
        PassengerRating passengerRating = passengerRatingRepository.findPassengerRatingByPassengerId(request.getPassengerId())
                .orElse(new PassengerRating());
        passengerRating.setPassenger(passengerService.getById(request.getPassengerId()));
        passengerRating.setRating(request.getPassengerRating());
        return passengerRatingRepository.save(passengerRating);
    }

    private PassengerRating getOrThrow(Long passengerId) {
        return passengerRatingRepository.findPassengerRatingByPassengerId(passengerId)
                .orElseThrow(() -> new PassengerRatingIsNotExistsException(String.format(RATING_NOT_EXISTS, passengerId)));
    }
}
