package com.software.modsen.driverservice.controller;

import com.software.modsen.driverservice.dto.request.CarChangeStatusRequest;
import com.software.modsen.driverservice.dto.request.CarCreateRequest;
import com.software.modsen.driverservice.dto.response.CarListResponse;
import com.software.modsen.driverservice.dto.response.CarResponse;
import com.software.modsen.driverservice.dto.request.CarUpdateRequest;
import com.software.modsen.driverservice.mapper.CarMapper;
import com.software.modsen.driverservice.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
public class CarController {
    private final CarService carService;
    private final CarMapper carMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'driver')")
    public Mono<ResponseEntity<CarListResponse>> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false, defaultValue = "id") String sortBy,
                                        @RequestParam(required = false, defaultValue = "false") Boolean isRestricted) {
        return carService.getAll(pageNumber, pageSize, sortBy, isRestricted)
                .collectList()
                .map(carMapper::toListResponse)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'driver')")
    public Mono<ResponseEntity<CarResponse>> getById(@PathVariable Long id) {
        return carService.getById(id)
                .map(carMapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Mono<ResponseEntity<CarResponse>> save(@RequestBody @Valid CarCreateRequest request) {
        return carService.save(carMapper.toEntity(request))
                .map(carMapper::toResponse)
                .map(response -> new ResponseEntity<>(response, HttpStatus.CREATED));
    }

    @PutMapping
    @PreAuthorize("hasRole('admin')")
    public Mono<ResponseEntity<CarResponse>> update(@RequestBody @Valid CarUpdateRequest request) {
        return carService.update(carMapper.toEntity(request))
                .map(carMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/restrict")
    @PreAuthorize("hasRole('admin')")
    public Mono<ResponseEntity<CarResponse>> changeRestrictionsStatus(@RequestBody CarChangeStatusRequest request) {
        return carService.changeRestrictionsStatus(request)
                .map(carMapper::toResponse)
                .map(ResponseEntity::ok);
    }
}