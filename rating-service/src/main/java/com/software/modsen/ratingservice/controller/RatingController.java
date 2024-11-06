package com.software.modsen.ratingservice.controller;

import com.software.modsen.ratingservice.dto.request.RatingDriverRequest;
import com.software.modsen.ratingservice.dto.request.RatingPassengerRequest;
import com.software.modsen.ratingservice.dto.response.RatingListResponse;
import com.software.modsen.ratingservice.dto.response.RatingResponse;
import com.software.modsen.ratingservice.entity.Rating;
import com.software.modsen.ratingservice.mapper.RatingMapper;
import com.software.modsen.ratingservice.service.impl.RatingServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ratings")
public class RatingController {
    private final RatingServiceImpl ratingService;
    private final RatingMapper ratingMapper;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<RatingListResponse> getAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                     @RequestParam(required = false, defaultValue = "id") String sortBy) {
        return new ResponseEntity<>(ratingMapper.toListResponse(ratingService.getAll(pageNumber, pageSize, sortBy)), HttpStatus.OK);
    }

    @PostMapping("/passenger")
    @PreAuthorize("hasRole('driver')")
    public ResponseEntity<RatingResponse> ratedPassenger(@RequestBody @Valid RatingPassengerRequest request) {
        Rating rating = ratingService.ratingPassenger(ratingMapper.toEntity(request));
        return new ResponseEntity<>(ratingMapper.toResponse(rating), HttpStatus.CREATED);
    }

    @PutMapping("/driver")
    @PreAuthorize("hasRole('passenger')")
    public ResponseEntity<RatingResponse> ratedDriver(@RequestBody @Valid RatingDriverRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long id = Long.valueOf(jwt.getClaim("userId"));
        Rating rating = ratingService.ratingDriver(id, request);
        return new ResponseEntity<>(ratingMapper.toResponse(rating), HttpStatus.OK);
    }
}
