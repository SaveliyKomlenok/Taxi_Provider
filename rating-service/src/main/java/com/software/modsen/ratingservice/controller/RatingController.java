package com.software.modsen.ratingservice.controller;

import com.software.modsen.ratingservice.dto.request.RatingDriverRequest;
import com.software.modsen.ratingservice.dto.request.RatingPassengerRequest;
import com.software.modsen.ratingservice.dto.response.DriverRatingResponse;
import com.software.modsen.ratingservice.dto.response.PassengerRatingResponse;
import com.software.modsen.ratingservice.dto.response.RatingListResponse;
import com.software.modsen.ratingservice.dto.response.RatingResponse;
import com.software.modsen.ratingservice.entity.Rating;
import com.software.modsen.ratingservice.mapper.RatingMapper;
import com.software.modsen.ratingservice.service.impl.RatingServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ratings")
public class RatingController {
    private final RatingServiceImpl ratingService;
    private final RatingMapper ratingMapper;

    @GetMapping
    public ResponseEntity<RatingListResponse> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false, defaultValue = "id") String sortBy) {
        return new ResponseEntity<>(ratingMapper.fromListEntityToListResponse(ratingService.getAll(pageNumber, pageSize, sortBy)), HttpStatus.OK);
    }

    @PostMapping("/passenger")
    public ResponseEntity<RatingResponse> ratedPassenger(@RequestBody @Valid RatingPassengerRequest request) {
        Rating rating = ratingService.ratingPassenger(ratingMapper.fromRequestToEntity(request));
        return new ResponseEntity<>(ratingMapper.fromEntityToResponse(rating), HttpStatus.CREATED);
    }

    @PatchMapping("/driver")
    public ResponseEntity<RatingResponse> ratedDriver(@RequestBody @Valid RatingDriverRequest request) {
        Rating rating = ratingService.ratingDriver(request);
        return new ResponseEntity<>(ratingMapper.fromEntityToResponse(rating), HttpStatus.OK);
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<PassengerRatingResponse> getPassengerRating(@PathVariable Long id){
        return new ResponseEntity<>(ratingService.calculatePassengerRating(id), HttpStatus.OK);
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<DriverRatingResponse> getDriverRating(@PathVariable Long id){
        return new ResponseEntity<>(ratingService.calculateDriverRating(id), HttpStatus.OK);
    }
}
