package com.software.modsen.ratingservice.service;

import com.software.modsen.ratingservice.dto.request.RatingDriverRequest;
import com.software.modsen.ratingservice.entity.Rating;
import com.software.modsen.ratingservice.exception.DriverAlreadyHasRatingException;
import com.software.modsen.ratingservice.exception.RatingDriverException;
import com.software.modsen.ratingservice.repository.RatingRepository;
import com.software.modsen.ratingservice.service.impl.RatingServiceImpl;
import com.software.modsen.ratingservice.util.RatingTestEntities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.software.modsen.ratingservice.util.NumericConstants.MINIMAL_RATING;
import static com.software.modsen.ratingservice.util.RatingTestEntities.AVERAGE_DRIVER_RATING;
import static com.software.modsen.ratingservice.util.RatingTestEntities.AVERAGE_PASSENGER_RATING;
import static com.software.modsen.ratingservice.util.RatingTestEntities.DRIVER_ID;
import static com.software.modsen.ratingservice.util.RatingTestEntities.DRIVER_RATING;
import static com.software.modsen.ratingservice.util.RatingTestEntities.EXPECTED_LIST_SIZE;
import static com.software.modsen.ratingservice.util.RatingTestEntities.FIRST_INDEX;
import static com.software.modsen.ratingservice.util.RatingTestEntities.PAGE_NUMBER;
import static com.software.modsen.ratingservice.util.RatingTestEntities.PAGE_SIZE;
import static com.software.modsen.ratingservice.util.RatingTestEntities.PASSENGER_ID;
import static com.software.modsen.ratingservice.util.RatingTestEntities.SORT_BY_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RatingServiceTest {
    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private PassengerRatingService passengerRatingService;

    @Mock
    private DriverRatingService driverRatingService;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private Rating rating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rating = RatingTestEntities.getTestRating();
    }

    @Test
    void getAll_ShouldReturnAllRatings() {
        when(ratingRepository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(SORT_BY_ID))))
                .thenReturn(new PageImpl<>(Collections.singletonList(rating)));

        List<Rating> result = ratingService.getAll(PAGE_NUMBER, PAGE_SIZE, SORT_BY_ID);

        assertEquals(EXPECTED_LIST_SIZE, result.size());
        assertEquals(rating, result.get(FIRST_INDEX));
        verify(ratingRepository).findAll(any(Pageable.class));
    }

    @Test
    void ratingPassenger_ShouldReturnSavedRating_WhenSuccessful() {
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating result = ratingService.ratingPassenger(rating);

        assertEquals(rating, result);
        verify(ratingRepository).save(rating);
        verify(passengerRatingService).savePassengerRating(any());
    }

    @Test
    void ratingDriver_ShouldReturnUpdatedRating_WhenSuccessful() {
        RatingDriverRequest request = RatingTestEntities.getDriverRatingRequestForIT();
        when(ratingRepository.findRatingByRideIdAndDriverIdAndPassengerId(
                request.getRideId(),
                request.getDriverId(),
                request.getPassengerId()))
                .thenReturn(Optional.of(rating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating result = ratingService.ratingDriver(request);

        assertEquals(rating, result);
        verify(ratingRepository).save(rating);
        verify(driverRatingService).saveDriverRating(any());
    }

    @Test
    void ratingDriver_ShouldThrowException_WhenDriverHasAlreadyRated() {
        rating.setDriverRating(DRIVER_RATING);
        RatingDriverRequest request = RatingTestEntities.getDriverRatingRequestForIT();
        when(ratingRepository.findRatingByRideIdAndDriverIdAndPassengerId(
                request.getRideId(),
                request.getDriverId(),
                request.getPassengerId()))
                .thenReturn(Optional.of(rating));

        assertThrows(DriverAlreadyHasRatingException.class, () -> ratingService.ratingDriver(request));
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void ratingDriver_ShouldThrowException_WhenRatingNotFound() {
        RatingDriverRequest request = RatingTestEntities.getDriverRatingRequestForIT();
        when(ratingRepository.findRatingByRideIdAndDriverIdAndPassengerId(
                request.getRideId(),
                request.getDriverId(),
                request.getPassengerId()))
                .thenReturn(Optional.empty());

        assertThrows(RatingDriverException.class, () -> ratingService.ratingDriver(request));
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void calculatePassengerRating_ShouldReturnAverage_WhenRatingsExist() {
        when(ratingRepository.findRatingsByPassengerIdAndPassengerRatingNotNull(PASSENGER_ID))
                .thenReturn(Collections.singletonList(rating));

        double average = ratingService.calculatePassengerRating(PASSENGER_ID);

        assertEquals(AVERAGE_PASSENGER_RATING, average);
    }

    @Test
    void calculatePassengerRating_ShouldReturnMinimal_WhenNoRatingsExist() {
        when(ratingRepository.findRatingsByPassengerIdAndPassengerRatingNotNull(PASSENGER_ID))
                .thenReturn(Collections.emptyList());

        double average = ratingService.calculatePassengerRating(PASSENGER_ID);

        assertEquals(MINIMAL_RATING, average);
    }

    @Test
    void calculateDriverRating_ShouldReturnAverage_WhenRatingsExist() {
        rating.setDriverRating(DRIVER_RATING);
        when(ratingRepository.findRatingsByDriverIdAndDriverRatingNotNull(DRIVER_ID))
                .thenReturn(Collections.singletonList(rating));

        double average = ratingService.calculateDriverRating(DRIVER_ID);

        assertEquals(AVERAGE_DRIVER_RATING, average);
    }

    @Test
    void calculateDriverRating_ShouldReturnMinimal_WhenNoRatingsExist() {
        when(ratingRepository.findRatingsByDriverIdAndDriverRatingNotNull(DRIVER_ID))
                .thenReturn(Collections.emptyList());

        double average = ratingService.calculateDriverRating(DRIVER_ID);

        assertEquals(MINIMAL_RATING, average);
    }
}
