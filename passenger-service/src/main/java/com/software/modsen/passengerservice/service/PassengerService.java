package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.dto.PassengerCreateRequest;
import com.software.modsen.passengerservice.dto.PassengerResponse;
import com.software.modsen.passengerservice.dto.PassengerUpdateRequest;

import java.util.List;

public interface PassengerService {
    PassengerResponse getById(Long id);

    List<PassengerResponse> getAll(Integer pageNumber, Integer pageSize, String sortBy);

    PassengerResponse save(PassengerCreateRequest request);

    PassengerResponse update(PassengerUpdateRequest request);

    PassengerResponse changeRestrictionsStatus(Long id);

    void delete(Long id);
}
