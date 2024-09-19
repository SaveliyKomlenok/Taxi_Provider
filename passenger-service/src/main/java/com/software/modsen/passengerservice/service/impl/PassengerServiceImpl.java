package com.software.modsen.passengerservice.service.impl;

import com.software.modsen.passengerservice.dto.PassengerCreateRequest;
import com.software.modsen.passengerservice.dto.PassengerResponse;
import com.software.modsen.passengerservice.dto.PassengerUpdateRequest;
import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.exception.PassengerIsNotExistsException;
import com.software.modsen.passengerservice.service.PassengerService;

import com.software.modsen.passengerservice.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final ModelMapper mapper;

    @Override
    public PassengerResponse getById(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerIsNotExistsException("Passenger with this id is not exists"));
        return mapper.map(passenger, PassengerResponse.class);
    }

    @Override
    public List<PassengerResponse> getAll(Integer pageNumber, Integer pageSize, String sortBy) {
        return passengerRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))).getContent()
                .stream()
                .map(passenger -> mapper.map(passenger, PassengerResponse.class))
                .toList();
    }

    @Override
    public PassengerResponse save(PassengerCreateRequest request) {
        Passenger passenger = passengerRepository.save(mapper.map(request, Passenger.class));
        return mapper.map(passenger, PassengerResponse.class);
    }

    @Override
    public PassengerResponse update(PassengerUpdateRequest request) {
        if (passengerRepository.findById(request.getId()).isPresent()) {
            Passenger passenger = passengerRepository.save(mapper.map(request, Passenger.class));
            return mapper.map(passenger, PassengerResponse.class);
        } else {
            throw new PassengerIsNotExistsException("Passenger with this id is not exists");
        }
    }

    @Override
    public PassengerResponse changeRestrictionsStatus(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerIsNotExistsException("Passenger with this id is not exists"));
        passenger.setRestricted(!passenger.isRestricted());
        passengerRepository.save(passenger);
        return mapper.map(passenger, PassengerResponse.class);
    }

    @Override
    public void delete(Long id) {
        if (!passengerRepository.existsById(id)) {
            throw new PassengerIsNotExistsException("Passenger with this id is not exists");
        }
        passengerRepository.deleteById(id);
    }
}
