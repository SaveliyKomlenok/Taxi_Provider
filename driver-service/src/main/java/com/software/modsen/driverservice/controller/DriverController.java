package com.software.modsen.driverservice.controller;

import com.software.modsen.driverservice.dto.DriverCreateRequest;
import com.software.modsen.driverservice.dto.DriverResponse;
import com.software.modsen.driverservice.dto.DriverUpdateRequest;
import com.software.modsen.driverservice.service.impl.DriverServiceImpl;
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
    private final DriverServiceImpl driverService;

    @GetMapping
    public ResponseEntity<List<DriverResponse>> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                    @RequestParam(required = false, defaultValue = "id") String sortBy) {
        List<DriverResponse> response = driverService.getAll(pageNumber, pageSize, sortBy);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getById(@PathVariable Long id) {
        DriverResponse response = driverService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DriverResponse> save(@RequestBody @Valid DriverCreateRequest request) {
        DriverResponse response = driverService.save(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<DriverResponse> update(@RequestBody @Valid DriverUpdateRequest request) {
        DriverResponse response = driverService.update(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DriverResponse> changeRestrictionsStatus(@PathVariable Long id){
        DriverResponse response = driverService.changeRestrictionsStatus(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        driverService.delete(id);
        return new ResponseEntity<>("Driver with id " + id + " successfully deleted", HttpStatus.OK);
    }
}
