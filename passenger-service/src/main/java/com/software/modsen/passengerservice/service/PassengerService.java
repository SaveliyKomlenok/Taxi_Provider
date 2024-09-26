package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.entity.Passenger;

import java.util.List;

public interface PassengerService {
    Passenger getById(Long id);

    List<Passenger> getAll(Integer pageNumber, Integer pageSize, String sortBy, Boolean includeRestricted);

    Passenger save(Passenger passenger);

    Passenger update(Passenger passenger);

    Passenger changeRestrictionsStatus(Long id);
}
