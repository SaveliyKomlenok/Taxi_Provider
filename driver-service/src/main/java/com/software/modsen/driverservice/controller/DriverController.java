package com.software.modsen.driverservice.controller;

import com.software.modsen.driverservice.dto.request.DriverChangeStatusRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
@RequestMapping("/api/v1/drivers")
public class DriverController {
    private final DriverService driverService;
    private final DriverMapper driverMapper;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DriverListResponse> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                     @RequestParam(required = false, defaultValue = "false") Boolean isRestricted) {
        List<Driver> driverList = driverService.getAll(pageNumber, pageSize, sortBy, isRestricted);
        return new ResponseEntity<>(driverMapper.toListResponse(driverList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'driver', 'passenger')")
    public ResponseEntity<DriverResponse> getById(@PathVariable Long id) {
        Driver driver = driverService.getById(id);
        return new ResponseEntity<>(driverMapper.toResponse(driver), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('realm-admin')")
    public ResponseEntity<DriverResponse> save(@RequestBody @Valid DriverCreateRequest request) {
        Driver driver = driverService.save(driverMapper.toEntity(request));
        return new ResponseEntity<>(driverMapper.toResponse(driver), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<DriverResponse> update(@RequestBody @Valid DriverUpdateRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long id = Long.valueOf(jwt.getClaim("userId"));
        Driver driver = driverService.update(id, driverMapper.toEntity(request));
        return new ResponseEntity<>(driverMapper.toResponse(driver), HttpStatus.OK);
    }

    @PutMapping("/restrict")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DriverResponse> changeRestrictionsStatus(@RequestBody DriverChangeStatusRequest request){
        Driver driver = driverService.changeRestrictionsStatus(request);
        return new ResponseEntity<>(driverMapper.toResponse(driver), HttpStatus.OK);
    }

    @PutMapping("/busy")
    @PreAuthorize("hasAnyRole('admin', 'driver', 'passenger')")
    public ResponseEntity<DriverResponse> changeBusyStatus(@RequestBody DriverChangeStatusRequest request){
        Driver driver = driverService.changeBusyStatus(request);
        return new ResponseEntity<>(driverMapper.toResponse(driver), HttpStatus.OK);
    }
}