package com.software.modsen.passengerservice.controller

import com.software.modsen.passengerservice.dto.request.PassengerRatingRequest
import com.software.modsen.passengerservice.dto.response.PassengerRatingResponse
import com.software.modsen.passengerservice.entity.PassengerRating
import com.software.modsen.passengerservice.mapper.PassengerRatingMapper
import com.software.modsen.passengerservice.service.PassengerRatingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/passenger-ratings")
@Tag(name = "Passengers rating", description = "Interaction with passengers rating")
open class PassengerRatingController(
    private val passengerRatingService: PassengerRatingService,
    private val ratingMapper: PassengerRatingMapper) {

    @Operation(summary = "Receiving passenger rating by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'driver', 'passenger')")
    open fun getPassengerRating(@PathVariable id: Long): ResponseEntity<PassengerRatingResponse> {
        val passengerRating: PassengerRating = passengerRatingService.getByPassengerId(id)
        return ResponseEntity(ratingMapper.toResponse(passengerRating), HttpStatus.OK)
    }

    @Operation(summary = "Saving passenger rating")
    @PostMapping
    @PreAuthorize("hasRole('driver')")
    open fun save(@RequestBody @Valid request: PassengerRatingRequest): ResponseEntity<PassengerRatingResponse> {
        val passengerRating: PassengerRating = passengerRatingService.save(ratingMapper.toEntity(request))
        return ResponseEntity(ratingMapper.toResponse(passengerRating), HttpStatus.CREATED)
    }
}