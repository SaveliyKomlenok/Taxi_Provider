package com.software.modsen.driverservice.controller;

import com.software.modsen.driverservice.dto.request.DriverCreateRequest;
import com.software.modsen.driverservice.dto.response.DriverListResponse;
import com.software.modsen.driverservice.dto.response.DriverResponse;
import com.software.modsen.driverservice.dto.request.DriverUpdateRequest;
import com.software.modsen.driverservice.entity.Driver;
import com.software.modsen.driverservice.mapper.DriverMapper;
import com.software.modsen.driverservice.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers")
public class DriverController {
    private final DriverService driverService;
    private final DriverMapper driverMapper;

    @GetMapping
    public ResponseEntity<DriverListResponse> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                     @RequestParam(required = false, defaultValue = "false") Boolean isRestricted) {
        List<Driver> driverList = driverService.getAll(pageNumber, pageSize, sortBy, isRestricted);
        return new ResponseEntity<>(driverMapper.toListResponse(driverList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getById(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        return new ResponseEntity<>(driverMapper.toResponse(driver), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DriverResponse> save(@RequestBody @Valid DriverCreateRequest request) {
        Driver driver = driverService.save(driverMapper.toEntity(request));
        return new ResponseEntity<>(driverMapper.toResponse(driver), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<DriverResponse> update(@RequestBody @Valid DriverUpdateRequest request) {
        Driver driver = driverService.update(driverMapper.toEntity(request));
        return new ResponseEntity<>(driverMapper.toResponse(driver), HttpStatus.OK);
    }

    @PutMapping("/restrict/{id}")
    public ResponseEntity<DriverResponse> changeRestrictionsStatus(@PathVariable Long id){
        Driver driver = driverService.changeRestrictionsStatus(id);
        return new ResponseEntity<>(driverMapper.toResponse(driver), HttpStatus.OK);
    }

    @PutMapping("/busy/{id}")
    public ResponseEntity<DriverResponse> changeBusyStatus(@PathVariable Long id){
        Driver driver = driverService.changeBusyStatus(id);
        return new ResponseEntity<>(driverMapper.toResponse(driver), HttpStatus.OK);
    }
}
