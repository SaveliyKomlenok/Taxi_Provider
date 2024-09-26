package com.software.modsen.passengerservice.controller;

import com.software.modsen.passengerservice.dto.request.PassengerCreateRequest;
import com.software.modsen.passengerservice.dto.response.PassengerListResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.dto.request.PassengerUpdateRequest;
import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/passengers")
public class PassengerController {
    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    @GetMapping
    public ResponseEntity<PassengerListResponse> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                        @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                        @RequestParam(required = false, defaultValue = "id") Boolean isRestricted) {
        List<Passenger> passengerList = passengerService.getAll(pageNumber, pageSize, sortBy, isRestricted);
        return new ResponseEntity<>(passengerMapper.toListResponse(passengerList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getById(@PathVariable Long id) {
        Passenger passenger = passengerService.getById(id);
        return new ResponseEntity<>(passengerMapper.toResponse(passenger), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> save(@RequestBody @Valid PassengerCreateRequest request) {
        Passenger passenger = passengerService.save(passengerMapper.toEntity(request));
        return new ResponseEntity<>(passengerMapper.toResponse(passenger), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PassengerResponse> update(@RequestBody @Valid PassengerUpdateRequest request) {
        Passenger passenger = passengerService.update(passengerMapper.toEntity(request));
        return new ResponseEntity<>(passengerMapper.toResponse(passenger), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PassengerResponse> changeRestrictionsStatus(@PathVariable Long id){
        Passenger passenger = passengerService.changeRestrictionsStatus(id);
        return new ResponseEntity<>(passengerMapper.toResponse(passenger), HttpStatus.OK);
    }
}
