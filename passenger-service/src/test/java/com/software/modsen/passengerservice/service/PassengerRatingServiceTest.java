package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.entity.PassengerRating;
import com.software.modsen.passengerservice.exception.PassengerRatingNotExistsException;
import com.software.modsen.passengerservice.repository.PassengerRatingRepository;
import com.software.modsen.passengerservice.service.impl.PassengerRatingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.software.modsen.passengerservice.util.ExceptionMessages.RATING_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PassengerRatingServiceTest {
    @Mock
    private PassengerRatingRepository passengerRatingRepository;

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private PassengerRatingServiceImpl passengerRatingService;

    private Passenger passenger;
    private PassengerRating passengerRating;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        passenger = Passenger.builder()
                .id(1L)
                .build();

        passengerRating = PassengerRating.builder()
                .passenger(passenger)
                .rating(5)
                .build();
    }

    @Test
    public void testGetByPassengerIdExists() {
        when(passengerRatingRepository.findPassengerRatingByPassengerId(passenger.getId()))
                .thenReturn(Optional.of(passengerRating));

        PassengerRating result = passengerRatingService.getByPassengerId(passenger.getId());

        assertNotNull(result);
        assertEquals(passenger.getId(), result.getPassenger().getId());
    }

    @Test
    public void testGetByPassengerIdNotExists() {
        when(passengerRatingRepository.findPassengerRatingByPassengerId(passenger.getId()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(PassengerRatingNotExistsException.class,
                () -> passengerRatingService.getByPassengerId(passenger.getId()));

        String expectedMessage = String.format(RATING_NOT_EXISTS, passenger.getId());
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSaveNewPassengerRating() {
        when(passengerRatingRepository.findPassengerRatingByPassengerId(passenger.getId()))
                .thenReturn(Optional.empty());
        when(passengerService.getById(passenger.getId())).thenReturn(passenger);
        when(passengerRatingRepository.save(any(PassengerRating.class))).thenReturn(passengerRating);

        PassengerRating savedRating = passengerRatingService.save(passengerRating);

        assertNotNull(savedRating);
        assertEquals(passenger, savedRating.getPassenger());
        assertEquals(5, savedRating.getRating());
        verify(passengerRatingRepository).save(any(PassengerRating.class));
    }

    @Test
    public void testSaveExistingPassengerRating() {
        when(passengerRatingRepository.findPassengerRatingByPassengerId(passenger.getId()))
                .thenReturn(Optional.of(passengerRating));
        when(passengerService.getById(passenger.getId())).thenReturn(passenger);
        when(passengerRatingRepository.save(passengerRating)).thenReturn(passengerRating);

        PassengerRating savedRating = passengerRatingService.save(passengerRating);

        assertNotNull(savedRating);
        assertEquals(passenger.getId(), savedRating.getPassenger().getId());
        assertEquals(5, savedRating.getRating());
        verify(passengerRatingRepository).save(passengerRating);
    }
}
