package com.software.modsen.rideservice.controller;

import com.software.modsen.rideservice.dto.request.RideCancelRequest;
import com.software.modsen.rideservice.dto.request.RideCreateRequest;
import com.software.modsen.rideservice.dto.request.RideStatusChangeRequest;
import com.software.modsen.rideservice.dto.response.RideListResponse;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.mapper.RideMapper;
import com.software.modsen.rideservice.service.impl.RideServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rides")
public class RideController {
    private final RideServiceImpl rideService;
    private final RideMapper rideMapper;

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getById(@PathVariable Long id) {
        return new ResponseEntity<>(rideMapper.fromEntityToResponse(rideService.getById(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<RideListResponse> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                   @RequestParam(required = false, defaultValue = "id") String sortBy){
        return new ResponseEntity<>(rideMapper.fromListEntityToListResponse(rideService.getAll(pageNumber, pageSize, sortBy)), HttpStatus.OK);
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<RideListResponse> getRidesByPassengerId(@PathVariable Long id) {
        return new ResponseEntity<>(rideMapper.fromListEntityToListResponse(
                rideService.getAllByPassengerId(id)), HttpStatus.OK);
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<RideListResponse> getRidesByDriverId(@PathVariable Long id) {
        return new ResponseEntity<>(rideMapper.fromListEntityToListResponse(
                rideService.getAllByDriverId(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RideResponse> create(@RequestBody @Valid RideCreateRequest request) {
        return new ResponseEntity<>(rideMapper.fromEntityToResponse(
                rideService.create(
                        rideMapper.fromResponseToEntity(request))), HttpStatus.CREATED);
    }

    @PatchMapping("/accept")
    public ResponseEntity<RideResponse> accept(@RequestBody @Valid RideStatusChangeRequest request) {
        return new ResponseEntity<>(rideMapper.fromEntityToResponse(
                rideService.accept(request)), HttpStatus.OK);
    }

    @PatchMapping("/finish")
    public ResponseEntity<RideResponse> finish(@RequestBody @Valid RideStatusChangeRequest request) {
        return new ResponseEntity<>(rideMapper.fromEntityToResponse(
                rideService.finish(request)), HttpStatus.OK);
    }

    @PatchMapping("/cancel")
    public ResponseEntity<RideResponse> cancel(@RequestBody @Valid RideCancelRequest request) {
        return new ResponseEntity<>(rideMapper.fromEntityToResponse(
                rideService.cancel(request)), HttpStatus.OK);
    }

    @PatchMapping("/change-status")
    public ResponseEntity<RideResponse> changeStatus(@RequestBody @Valid RideStatusChangeRequest request) {
        return new ResponseEntity<>(rideMapper.fromEntityToResponse(
                rideService.changeStatus(request)), HttpStatus.OK);
    }
}
