package com.software.modsen.driverservice.controller;

import com.software.modsen.driverservice.dto.CarCreateRequest;
import com.software.modsen.driverservice.dto.CarResponse;
import com.software.modsen.driverservice.dto.CarUpdateRequest;
import com.software.modsen.driverservice.service.impl.CarServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
public class CarController {
    private final CarServiceImpl carService;

    @GetMapping
    public ResponseEntity<List<CarResponse>> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                    @RequestParam(required = false, defaultValue = "id") String sortBy) {
        List<CarResponse> response = carService.getAll(pageNumber, pageSize, sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getById(@PathVariable Long id) {
        CarResponse response = carService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CarResponse> save(@RequestBody @Valid CarCreateRequest request) {
        CarResponse response = carService.save(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CarResponse> update(@RequestBody @Valid CarUpdateRequest request) {
        CarResponse response = carService.update(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CarResponse> changeRestrictionsStatus(@PathVariable Long id){
        CarResponse response = carService.changeRestrictionsStatus(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        carService.delete(id);
        return new ResponseEntity<>("Car with id " + id + " successfully deleted", HttpStatus.OK);
    }
}
