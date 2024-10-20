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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rides")
public class RideController {
    private final RideServiceImpl rideService;
    private final RideMapper rideMapper;

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getById(@PathVariable Long id) {
        Ride ride = rideService.getById(id);
        return new ResponseEntity<>(rideMapper.toResponse(ride), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RideListResponse> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                   @RequestParam(required = false, defaultValue = "id") String sortBy){
        List<Ride> rideList = rideService.getAll(pageNumber, pageSize, sortBy);
        return new ResponseEntity<>(rideMapper.toListResponse(rideList), HttpStatus.OK);
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<RideListResponse> getRidesByPassengerId(@PathVariable Long id) {
        List<Ride> rideList = rideService.getAllByPassengerId(id);
        return new ResponseEntity<>(rideMapper.toListResponse(rideList), HttpStatus.OK);
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<RideListResponse> getRidesByDriverId(@PathVariable Long id) {
        List<Ride> rideList = rideService.getAllByDriverId(id);
        return new ResponseEntity<>(rideMapper.toListResponse(rideList), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RideResponse> create(@RequestBody @Valid RideCreateRequest request) {
        Ride ride = rideService.create(rideMapper.toEntity(request));
        return new ResponseEntity<>(rideMapper.toResponse(ride), HttpStatus.CREATED);
    }

    @PostMapping("/accept")
    public ResponseEntity<RideResponse> accept(@RequestBody @Valid RideStatusChangeRequest request) {
        Ride ride = rideService.accept(request);
        return new ResponseEntity<>(rideMapper.toResponse(ride), HttpStatus.OK);
    }

    @PostMapping("/finish")
    public ResponseEntity<RideResponse> finish(@RequestBody @Valid RideFinishRequest request) {
        Ride ride = rideService.finish(request);
        return new ResponseEntity<>(rideMapper.toResponse(ride), HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<RideResponse> cancel(@RequestBody @Valid RideCancelRequest request) {
        Ride ride = rideService.cancel(request);
        return new ResponseEntity<>(rideMapper.toResponse(ride), HttpStatus.OK);
    }

    @PostMapping("/change-status")
    public ResponseEntity<RideResponse> changeStatus(@RequestBody @Valid RideStatusChangeRequest request) {
        Ride ride = rideService.changeStatus(request);
        return new ResponseEntity<>(rideMapper.toResponse(ride), HttpStatus.OK);
    }
}
