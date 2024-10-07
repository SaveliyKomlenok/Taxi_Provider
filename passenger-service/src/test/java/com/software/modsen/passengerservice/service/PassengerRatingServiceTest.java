package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.entity.PassengerRating;
import com.software.modsen.passengerservice.exception.PassengerRatingNotExistsException;
import com.software.modsen.passengerservice.repository.PassengerRatingRepository;
import com.software.modsen.passengerservice.service.impl.PassengerRatingServiceImpl;
import com.software.modsen.passengerservice.util.TestEntities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.software.modsen.passengerservice.util.ExceptionMessages.RATING_NOT_EXISTS;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_ID;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_RATING;
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
        passenger = TestEntities.getTestPassenger();
        passengerRating = TestEntities.getTestPassengerRating();
    }

    @Test
    public void testGetByPassengerIdExists() {
        when(passengerRatingRepository.findPassengerRatingByPassengerId(PASSENGER_ID))
                .thenReturn(Optional.of(passengerRating));

        PassengerRating result = passengerRatingService.getByPassengerId(PASSENGER_ID);

        assertNotNull(result);
        assertEquals(passenger.getId(), result.getPassenger().getId());
    }

    @Test
    public void testGetByPassengerIdNotExists() {
        when(passengerRatingRepository.findPassengerRatingByPassengerId(PASSENGER_ID))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(PassengerRatingNotExistsException.class,
                () -> passengerRatingService.getByPassengerId(PASSENGER_ID));

        String expectedMessage = String.format(RATING_NOT_EXISTS, PASSENGER_ID);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSaveNewPassengerRating() {
        when(passengerRatingRepository.findPassengerRatingByPassengerId(PASSENGER_ID))
                .thenReturn(Optional.empty());
        when(passengerService.getById(PASSENGER_ID)).thenReturn(passenger);
        when(passengerRatingRepository.save(any(PassengerRating.class))).thenReturn(passengerRating);

        PassengerRating savedRating = passengerRatingService.save(passengerRating);

        assertNotNull(savedRating);
        assertEquals(passenger, savedRating.getPassenger());
        assertEquals(PASSENGER_RATING, savedRating.getRating());
        verify(passengerRatingRepository).save(any(PassengerRating.class));
    }

    @Test
    public void testSaveExistingPassengerRating() {
        when(passengerRatingRepository.findPassengerRatingByPassengerId(PASSENGER_ID))
                .thenReturn(Optional.of(passengerRating));
        when(passengerService.getById(PASSENGER_ID)).thenReturn(passenger);
        when(passengerRatingRepository.save(passengerRating)).thenReturn(passengerRating);

        PassengerRating savedRating = passengerRatingService.save(passengerRating);

        assertNotNull(savedRating);
        assertEquals(PASSENGER_ID, savedRating.getPassenger().getId());
        assertEquals(PASSENGER_RATING, savedRating.getRating());
        verify(passengerRatingRepository).save(passengerRating);
    }
}
