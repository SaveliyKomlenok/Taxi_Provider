package com.software.modsen.driverservice.controller;

import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.dto.response.DriverRatingResponse;
import com.software.modsen.driverservice.entity.DriverRating;
import com.software.modsen.driverservice.mapper.DriverRatingMapper;
import com.software.modsen.driverservice.service.DriverRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/driver-ratings")
public class DriverRatingController {
    private final DriverRatingService driverRatingService;
    private final DriverRatingMapper driverRatingMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'driver', 'passenger')")
    public ResponseEntity<DriverRatingResponse> getDriverRating(@PathVariable Long id){
        DriverRating driverRating = driverRatingService.getByDriverId(id);
        return new ResponseEntity<>(driverRatingMapper.toResponse(driverRating), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('passenger')")
    public ResponseEntity<DriverRatingResponse> save(@RequestBody @Valid DriverRatingRequest request){
        DriverRating driverRating = driverRatingService.save(driverRatingMapper.toEntity(request));
        return new ResponseEntity<>(driverRatingMapper.toResponse(driverRating), HttpStatus.OK);
    }
}
