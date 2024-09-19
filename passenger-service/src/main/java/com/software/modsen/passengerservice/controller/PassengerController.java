package com.software.modsen.passengerservice.controller;

import com.software.modsen.passengerservice.dto.PassengerCreateRequest;
import com.software.modsen.passengerservice.dto.PassengerResponse;
import com.software.modsen.passengerservice.dto.PassengerUpdateRequest;
import com.software.modsen.passengerservice.service.impl.PassengerServiceImpl;
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
    private final PassengerServiceImpl passengerService;

    @GetMapping
    public ResponseEntity<List<PassengerResponse>> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                          @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                          @RequestParam(required = false, defaultValue = "id") String sortBy) {
        List<PassengerResponse> response = passengerService.getAll(pageNumber, pageSize, sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getById(@PathVariable Long id) {
        PassengerResponse response = passengerService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PassengerResponse> save(@RequestBody @Valid PassengerCreateRequest request) {
        PassengerResponse response = passengerService.save(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PassengerResponse> update(@RequestBody @Valid PassengerUpdateRequest request) {
        PassengerResponse response = passengerService.update(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PassengerResponse> changeRestrictionsStatus(@PathVariable Long id){
        PassengerResponse response = passengerService.changeRestrictionsStatus(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        passengerService.delete(id);
        return new ResponseEntity<>("Passenger with id " + id + " successfully deleted", HttpStatus.OK);
    }
}
