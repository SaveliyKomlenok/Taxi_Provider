package com.software.modsen.passengerservice.controller;

import com.software.modsen.passengerservice.dto.request.PassengerCreateRequest;
import com.software.modsen.passengerservice.dto.request.PassengerUpdateRequest;
import com.software.modsen.passengerservice.dto.response.PassengerListResponse;
import com.software.modsen.passengerservice.dto.response.PassengerResponse;
import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.mapper.PassengerMapper;
import com.software.modsen.passengerservice.service.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/passengers")
@Tag(name = "Passengers", description = "Interaction with passengers")
public class PassengerController {
    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    @Operation(summary = "Receiving all passengers")
    @GetMapping
    public ResponseEntity<PassengerListResponse> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                        @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                        @RequestParam(required = false) Boolean isRestricted) {
        List<Passenger> passengerList = passengerService.getAll(pageNumber, pageSize, sortBy, isRestricted);
        return new ResponseEntity<>(passengerMapper.toListResponse(passengerList), HttpStatus.OK);
    }

    @Operation(summary = "Receiving passenger by ID")
    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getById(@PathVariable Long id) {
        Passenger passenger = passengerService.getById(id);
        return new ResponseEntity<>(passengerMapper.toResponse(passenger), HttpStatus.OK);
    }

    @Operation(summary = "Saving passenger")
    @PostMapping
    public ResponseEntity<PassengerResponse> save(@RequestBody @Valid PassengerCreateRequest request) {
        Passenger passenger = passengerService.save(passengerMapper.toEntity(request));
        return new ResponseEntity<>(passengerMapper.toResponse(passenger), HttpStatus.CREATED);
    }

    @Operation(summary = "Updating passenger")
    @PutMapping
    public ResponseEntity<PassengerResponse> update(@RequestBody @Valid PassengerUpdateRequest request) {
        Passenger passenger = passengerService.update(passengerMapper.toEntity(request));
        return new ResponseEntity<>(passengerMapper.toResponse(passenger), HttpStatus.OK);
    }

    @Operation(summary = "Changing restrict status of passenger")
    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponse> changeRestrictionsStatus(@PathVariable Long id) {
        Passenger passenger = passengerService.changeRestrictionsStatus(id);
        return new ResponseEntity<>(passengerMapper.toResponse(passenger), HttpStatus.OK);
    }
}
