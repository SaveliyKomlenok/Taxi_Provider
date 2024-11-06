package com.software.modsen.passengerservice.controller

import com.software.modsen.passengerservice.dto.request.PassengerChangeStatusRequest
import com.software.modsen.passengerservice.dto.request.PassengerCreateRequest
import com.software.modsen.passengerservice.dto.request.PassengerUpdateRequest
import com.software.modsen.passengerservice.dto.response.PassengerListResponse
import com.software.modsen.passengerservice.dto.response.PassengerResponse
import com.software.modsen.passengerservice.entity.Passenger
import com.software.modsen.passengerservice.mapper.PassengerMapper
import com.software.modsen.passengerservice.service.PassengerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/passengers")
@Tag(name = "Passengers", description = "Interaction with passengers")
open class PassengerController(private val passengerService: PassengerService, private val passengerMapper: PassengerMapper) {

    @Operation(summary = "Receiving all passengers")
    @GetMapping
    @PreAuthorize("hasRole('admin')")
    open fun getAll(
        @RequestParam(defaultValue = "0") pageNumber: Int = 0,
        @RequestParam(defaultValue = "10") pageSize: Int = 10,
        @RequestParam(defaultValue = "id") sortBy: String = "id",
        @RequestParam(defaultValue = "false")  isRestricted: Boolean
    ): ResponseEntity<PassengerListResponse> {
        val passengerList: List<Passenger> = passengerService.getAll(pageNumber, pageSize, sortBy, isRestricted)
        return ResponseEntity(passengerMapper.toListResponse(passengerList), HttpStatus.OK)
    }

    @Operation(summary = "Receiving passenger by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'driver', 'passenger')")
    open fun getById(@PathVariable id: Long): ResponseEntity<PassengerResponse> {
        val passenger: Passenger = passengerService.getById(id)
        return ResponseEntity(passengerMapper.toResponse(passenger), HttpStatus.OK)
    }

    @Operation(summary = "Saving passenger")
    @PostMapping
    @PreAuthorize("hasRole('realm-admin')")
    open fun save(@Valid @RequestBody request: PassengerCreateRequest): ResponseEntity<PassengerResponse> {
        val passenger: Passenger = passengerService.save(passengerMapper.toEntity(request))
        return ResponseEntity(passengerMapper.toResponse(passenger), HttpStatus.CREATED)
    }

    @Operation(summary = "Updating passenger")
    @PutMapping
    @PreAuthorize("hasRole('passenger')")
    open fun update(@Valid @RequestBody request: PassengerUpdateRequest, @AuthenticationPrincipal jwt: Jwt): ResponseEntity<PassengerResponse> {
        val id: Long = jwt.getClaim<String>("userId").toLong()
        val passenger: Passenger = passengerService.update(id, passengerMapper.toEntity(request))
        return ResponseEntity(passengerMapper.toResponse(passenger), HttpStatus.OK)
    }

    @Operation(summary = "Changing restrict status of passenger")
    @PutMapping("/restrict")
    @PreAuthorize("hasRole('admin')")
    open fun changeRestrictionsStatus(
        @RequestBody request: PassengerChangeStatusRequest
    ): ResponseEntity<PassengerResponse> {
        val passenger: Passenger = passengerService.changeRestrictionsStatus(request)
        return ResponseEntity(passengerMapper.toResponse(passenger), HttpStatus.OK)
    }
}