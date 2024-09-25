package com.software.modsen.passengerservice.controller;

import com.software.modsen.passengerservice.dto.request.PassengerRatingRequest;
import com.software.modsen.passengerservice.dto.response.PassengerRatingResponse;
import com.software.modsen.passengerservice.entity.PassengerRating;
import com.software.modsen.passengerservice.mapper.PassengerRatingMapper;
import com.software.modsen.passengerservice.service.impl.PassengerRatingServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/passenger-ratings")
public class PassengerRatingController {
    private final PassengerRatingServiceImpl passengerRatingService;
    private final PassengerRatingMapper ratingMapper;

    @GetMapping("/{id}")
    public ResponseEntity<PassengerRatingResponse> getPassengerRating(@PathVariable Long id){
        PassengerRating passengerRating = passengerRatingService.getByPassengerId(id);
        return new ResponseEntity<>(ratingMapper.fromEntityToResponse(passengerRating), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PassengerRatingResponse> save(@RequestBody @Valid PassengerRatingRequest request){
        PassengerRating passengerRating = passengerRatingService.save(request);
        return new ResponseEntity<>(ratingMapper.fromEntityToResponse(passengerRating), HttpStatus.OK);
    }
}
