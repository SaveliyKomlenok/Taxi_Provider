package com.software.modsen.rideservice.controller;

import com.software.modsen.rideservice.dto.request.RideCancelRequest;
import com.software.modsen.rideservice.dto.request.RideCreateRequest;
import com.software.modsen.rideservice.dto.request.RideFinishRequest;
import com.software.modsen.rideservice.dto.request.RideStatusChangeRequest;
import com.software.modsen.rideservice.dto.response.RideListResponse;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.entity.Ride;
import com.software.modsen.rideservice.mapper.RideMapper;
import com.software.modsen.rideservice.service.impl.RideServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rides")
public class RideController {
    private final RideServiceImpl rideService;
    private final RideMapper rideMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('driver', 'passenger')")
    public ResponseEntity<RideResponse> getById(@PathVariable Long id) {
        Ride ride = rideService.getById(id);
        return new ResponseEntity<>(rideMapper.toResponse(ride), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<RideListResponse> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                   @RequestParam(required = false, defaultValue = "id") String sortBy){
        List<Ride> rideList = rideService.getAll(pageNumber, pageSize, sortBy);
        return new ResponseEntity<>(rideMapper.toListResponse(rideList), HttpStatus.OK);
    }

    @GetMapping("/passenger")
    @PreAuthorize("hasRole('passenger')")
    public ResponseEntity<RideListResponse> getRidesByPassengerId(@AuthenticationPrincipal Jwt jwt) {
        Long id = Long.valueOf(jwt.getClaim("userId"));
        List<Ride> rideList = rideService.getAllByPassengerId(id);
        return new ResponseEntity<>(rideMapper.toListResponse(rideList), HttpStatus.OK);
    }

    @GetMapping("/driver")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<RideListResponse> getRidesByDriverId(@AuthenticationPrincipal Jwt jwt) {
        Long id = Long.valueOf(jwt.getClaim("userId"));
        List<Ride> rideList = rideService.getAllByDriverId(id);
        return new ResponseEntity<>(rideMapper.toListResponse(rideList), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('passenger')")
    public ResponseEntity<RideResponse> create(@RequestBody @Valid RideCreateRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long id = Long.valueOf(jwt.getClaim("userId"));
        Ride ride = rideService.create(id, rideMapper.toEntity(request));
        return new ResponseEntity<>(rideMapper.toResponse(ride), HttpStatus.CREATED);
    }

    @PostMapping("/accept")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<RideResponse> accept(@RequestBody @Valid RideStatusChangeRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long id = Long.valueOf(jwt.getClaim("userId"));
        Ride ride = rideService.accept(id, request);
        return new ResponseEntity<>(rideMapper.toResponse(ride), HttpStatus.OK);
    }

    @PostMapping("/finish")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<RideResponse> finish(@RequestBody @Valid RideFinishRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long id = Long.valueOf(jwt.getClaim("userId"));
        Ride ride = rideService.finish(id, request);
        return new ResponseEntity<>(rideMapper.toResponse(ride), HttpStatus.OK);
    }

    @PostMapping("/cancel")
    @PreAuthorize("hasRole('passenger')")
    public ResponseEntity<RideResponse> cancel(@RequestBody @Valid RideCancelRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long id = Long.valueOf(jwt.getClaim("userId"));
        Ride ride = rideService.cancel(id, request);
        return new ResponseEntity<>(rideMapper.toResponse(ride), HttpStatus.OK);
    }

    @PostMapping("/change-status")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<RideResponse> changeStatus(@RequestBody @Valid RideStatusChangeRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long id = Long.valueOf(jwt.getClaim("userId"));
        Ride ride = rideService.changeStatus(id, request);
        return new ResponseEntity<>(rideMapper.toResponse(ride), HttpStatus.OK);
    }
}
