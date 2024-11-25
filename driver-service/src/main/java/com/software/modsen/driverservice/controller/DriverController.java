package com.software.modsen.driverservice.controller;

import com.software.modsen.driverservice.dto.request.DriverChangeStatusRequest;
import com.software.modsen.driverservice.dto.request.DriverCreateRequest;
import com.software.modsen.driverservice.dto.response.DriverListResponse;
import com.software.modsen.driverservice.dto.response.DriverResponse;
import com.software.modsen.driverservice.dto.request.DriverUpdateRequest;
import com.software.modsen.driverservice.mapper.DriverMapper;
import com.software.modsen.driverservice.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers")
public class DriverController {
    private final DriverService driverService;
    private final DriverMapper driverMapper;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public Mono<ResponseEntity<DriverListResponse>> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                           @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                           @RequestParam(required = false, defaultValue = "false") Boolean isRestricted) {
        return driverService.getAll(pageNumber, pageSize, sortBy, isRestricted)
                .collectList()
                .map(driverList -> ResponseEntity.ok(driverMapper.toListResponse(driverList)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'driver', 'passenger')")
    public Mono<ResponseEntity<DriverResponse>> getById(@PathVariable Long id) {
        return driverService.getById(id)
                .map(driver -> ResponseEntity.ok(driverMapper.toResponse(driver)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('realm-admin')")
    public Mono<ResponseEntity<DriverResponse>> save(@RequestBody @Valid DriverCreateRequest request) {
        return driverService.save(driverMapper.toEntity(request))
                .map(driver -> ResponseEntity.status(HttpStatus.CREATED).body(driverMapper.toResponse(driver)));
    }

    @PutMapping
    @PreAuthorize("hasRole('driver')")
    public Mono<ResponseEntity<DriverResponse>> update(@RequestBody @Valid DriverUpdateRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long id = Long.valueOf(jwt.getClaim("userId"));
        return driverService.update(id, driverMapper.toEntity(request))
                .map(driver -> ResponseEntity.ok(driverMapper.toResponse(driver)));
    }

    @PutMapping("/restrict")
    @PreAuthorize("hasRole('admin')")
    public Mono<ResponseEntity<DriverResponse>> changeRestrictionsStatus(@RequestBody DriverChangeStatusRequest request) {
        return driverService.changeRestrictionsStatus(request)
                .map(driver -> ResponseEntity.ok(driverMapper.toResponse(driver)));
    }

    @PutMapping("/busy")
    @PreAuthorize("hasAnyRole('admin', 'driver', 'passenger')")
    public Mono<ResponseEntity<DriverResponse>> changeBusyStatus(@RequestBody DriverChangeStatusRequest request) {
        return driverService.changeBusyStatus(request)
                .map(driver -> ResponseEntity.ok(driverMapper.toResponse(driver)));
    }
}