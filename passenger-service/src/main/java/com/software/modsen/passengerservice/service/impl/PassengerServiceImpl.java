package com.software.modsen.passengerservice.service.impl;

import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.exception.PassengerAlreadyExistsException;
import com.software.modsen.passengerservice.exception.PassengerNotExistsException;
import com.software.modsen.passengerservice.repository.PassengerRepository;
import com.software.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_ALREADY_EXISTS;
import static com.software.modsen.passengerservice.util.ExceptionMessages.PASSENGER_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;

    @Override
    public Passenger getById(Long id) {
        return getOrThrow(id);
    }

    @Override
    public List<Passenger> getAll(Integer pageNumber, Integer pageSize, String sortBy, Boolean includeRestricted) {
        if (includeRestricted != null && includeRestricted) {
            return passengerRepository.findAllByRestrictedIsTrue(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy)));
        } else {
            return passengerRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))).getContent();
        }
    }

    @Override
    public Passenger save(Passenger passenger) {
        if (passengerRepository.findPassengerByEmailAndPhoneNumber(
                passenger.getEmail(),
                passenger.getPhoneNumber()).isPresent()) {
            throw new PassengerAlreadyExistsException(PASSENGER_ALREADY_EXISTS);
        }
        return passengerRepository.save(passenger);
    }

    @Override
    public Passenger update(Passenger passenger) {
        getOrThrow(passenger.getId());
        Passenger existingPassenger = passengerRepository.findPassengerByEmailAndPhoneNumber(
                        passenger.getEmail(),
                        passenger.getPhoneNumber())
                .orElse(null);
        if (existingPassenger != null && !existingPassenger.getId().equals(passenger.getId())) {
            throw new PassengerAlreadyExistsException(PASSENGER_ALREADY_EXISTS);
        }
        return passengerRepository.save(passenger);
    }

    @Override
    public Passenger changeRestrictionsStatus(Long id) {
        Passenger passenger = getOrThrow(id);
        passenger.setRestricted(!passenger.isRestricted());
        return passengerRepository.save(passenger);
    }

    private Passenger getOrThrow(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotExistsException(String.format(PASSENGER_NOT_EXISTS, id)));
    }
}
