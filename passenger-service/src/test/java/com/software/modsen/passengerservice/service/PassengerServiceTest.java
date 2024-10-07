package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.exception.PassengerAlreadyExistsException;
import com.software.modsen.passengerservice.exception.PassengerNotExistsException;
import com.software.modsen.passengerservice.repository.PassengerRepository;
import com.software.modsen.passengerservice.service.impl.PassengerServiceImpl;
import com.software.modsen.passengerservice.util.TestEntities;
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

import static com.software.modsen.passengerservice.util.TestEntities.EXPECTED_LIST_SIZE;
import static com.software.modsen.passengerservice.util.TestEntities.FIRST_INDEX;
import static com.software.modsen.passengerservice.util.TestEntities.PAGE_NUMBER;
import static com.software.modsen.passengerservice.util.TestEntities.PAGE_SIZE;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_EMAIL;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_ID;
import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_PHONE_NUMBER;
import static com.software.modsen.passengerservice.util.TestEntities.SORT_BY_ID;
import static com.software.modsen.passengerservice.util.TestEntities.WANTED_NUMBER_OF_INVOCATIONS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PassengerServiceTest {
    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    private Passenger passenger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passenger = TestEntities.getTestPassenger();
    }

    @Test
    void getById_ShouldReturnPassenger_WhenExists() {
        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passenger));

        Passenger result = passengerService.getById(PASSENGER_ID);

        assertEquals(passenger, result);
        verify(passengerRepository).findById(PASSENGER_ID);
    }

    @Test
    void getById_ShouldThrowException_WhenNotExists() {
        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.empty());

        assertThrows(PassengerNotExistsException.class, () -> passengerService.getById(PASSENGER_ID));
        verify(passengerRepository).findById(PASSENGER_ID);
    }

    @Test
    void getAll_ShouldReturnAllPassengers_WhenIncludeRestrictedIsFalse() {
        when(passengerRepository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(SORT_BY_ID))))
                .thenReturn(new PageImpl<>(Collections.singletonList(passenger)));

        List<Passenger> result = passengerService.getAll(PAGE_NUMBER, PAGE_SIZE, SORT_BY_ID, false);

        assertEquals(EXPECTED_LIST_SIZE, result.size());
        assertEquals(passenger, result.get(FIRST_INDEX));
        verify(passengerRepository).findAll(any(Pageable.class));
    }

    @Test
    void save_ShouldReturnPassenger_WhenSavedSuccessfully() {
        when(passengerRepository.findPassengerByEmailAndPhoneNumber(PASSENGER_EMAIL, PASSENGER_PHONE_NUMBER))
                .thenReturn(Optional.empty());
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        Passenger result = passengerService.save(passenger);

        assertEquals(passenger, result);
        verify(passengerRepository).save(passenger);
    }

    @Test
    void save_ShouldThrowException_WhenPassengerAlreadyExists() {
        when(passengerRepository.findPassengerByEmailAndPhoneNumber(PASSENGER_EMAIL, PASSENGER_PHONE_NUMBER))
                .thenReturn(Optional.of(passenger));

        assertThrows(PassengerAlreadyExistsException.class, () -> passengerService.save(passenger));
        verify(passengerRepository, never()).save(any());
    }

    @Test
    void update_ShouldReturnUpdatedPassenger_WhenUpdateIsSuccessful() {
        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passenger));
        when(passengerRepository.findPassengerByEmailAndPhoneNumber(PASSENGER_EMAIL, PASSENGER_PHONE_NUMBER))
                .thenReturn(Optional.empty());
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        Passenger result = passengerService.update(passenger);

        assertEquals(passenger, result);
        verify(passengerRepository).save(passenger);
    }

    @Test
    void update_ShouldThrowException_WhenPassengerAlreadyExists() {
        Passenger secondPassenger = TestEntities.getSecondTestPassenger();
        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passenger));
        when(passengerRepository.findPassengerByEmailAndPhoneNumber(PASSENGER_EMAIL, PASSENGER_PHONE_NUMBER))
                .thenReturn(Optional.of(secondPassenger));

        assertThrows(PassengerAlreadyExistsException.class, () -> passengerService.update(passenger));
        verify(passengerRepository, never()).save(any());
    }

    @Test
    void changeRestrictionsStatus_ShouldChangeStatus_WhenPassengerExists() {
        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passenger));
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        Passenger updatedPassenger = passengerService.changeRestrictionsStatus(PASSENGER_ID);

        assertTrue(updatedPassenger.isRestricted());
        verify(passengerRepository, times(WANTED_NUMBER_OF_INVOCATIONS)).save(passenger);
    }

    @Test
    void changeRestrictionsStatus_ShouldThrowException_WhenPassengerDoesNotExist() {
        when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.empty());

        assertThrows(PassengerNotExistsException.class, () -> passengerService.changeRestrictionsStatus(PASSENGER_ID));
        verify(passengerRepository, never()).save(any());
    }
}
