package com.software.modsen.driverservice.controller;

import com.software.modsen.driverservice.dto.request.CarCreateRequest;
import com.software.modsen.driverservice.dto.response.CarListResponse;
import com.software.modsen.driverservice.dto.response.CarResponse;
import com.software.modsen.driverservice.dto.request.CarUpdateRequest;
import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.mapper.CarMapper;
import com.software.modsen.driverservice.service.CarService;
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
    private final CarService carService;
    private final CarMapper carMapper;

    @GetMapping
    public ResponseEntity<CarListResponse> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                  @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                  @RequestParam(required = false, defaultValue = "false") Boolean isRestricted) {
        List<Car> carList = carService.getAll(pageNumber, pageSize, sortBy, isRestricted);
        return new ResponseEntity<>(carMapper.toListResponse(carList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getById(@PathVariable Long id) {
        Car car = carService.getById(id);
        return new ResponseEntity<>(carMapper.toResponse(car), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CarResponse> save(@RequestBody @Valid CarCreateRequest request) {
        Car car = carService.save(carMapper.toEntity(request));
        return new ResponseEntity<>(carMapper.toResponse(car), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CarResponse> update(@RequestBody @Valid CarUpdateRequest request) {
        Car car = carService.update(carMapper.toEntity(request));
        return new ResponseEntity<>(carMapper.toResponse(car), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> changeRestrictionsStatus(@PathVariable Long id){
        Car car = carService.changeRestrictionsStatus(id);
        return new ResponseEntity<>(carMapper.toResponse(car), HttpStatus.OK);
    }
}
