package com.software.modsen.passengerservice.service;

import com.software.modsen.passengerservice.entity.Passenger;
import com.software.modsen.passengerservice.exception.PassengerAlreadyExistsException;
import com.software.modsen.passengerservice.exception.PassengerNotExistsException;
import com.software.modsen.passengerservice.repository.PassengerRepository;
import com.software.modsen.passengerservice.service.impl.PassengerServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
        passenger = Passenger.builder()
                .id(1L)
                .email("test@example.com")
                .phoneNumber("+375331234567")
                .build();
    }

    @Test
    void getById_ShouldReturnPassenger_WhenExists() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        Passenger result = passengerService.getById(1L);

        assertEquals(passenger, result);
        verify(passengerRepository).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotExists() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PassengerNotExistsException.class, () -> passengerService.getById(1L));
        verify(passengerRepository).findById(1L);
    }

    @Test
    void getAll_ShouldReturnAllPassengers_WhenIncludeRestrictedIsFalse() {
        when(passengerRepository.findAll(PageRequest.of(0, 10, Sort.by("id"))))
                .thenReturn(new PageImpl<>(Collections.singletonList(passenger)));

        List<Passenger> result = passengerService.getAll(0, 10, "id", false);

        assertEquals(1, result.size());
        assertEquals(passenger, result.get(0));
        verify(passengerRepository).findAll(any(Pageable.class));
    }

    @Test
    void save_ShouldReturnPassenger_WhenSavedSuccessfully() {
        when(passengerRepository.findPassengerByEmailAndPhoneNumber("test@example.com", "+375331234567"))
                .thenReturn(Optional.empty());
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        Passenger result = passengerService.save(passenger);

        assertEquals(passenger, result);
        verify(passengerRepository).save(passenger);
    }

    @Test
    void save_ShouldThrowException_WhenPassengerAlreadyExists() {
        when(passengerRepository.findPassengerByEmailAndPhoneNumber("test@example.com", "+375331234567"))
                .thenReturn(Optional.of(passenger));

        assertThrows(PassengerAlreadyExistsException.class, () -> passengerService.save(passenger));
        verify(passengerRepository, never()).save(any());
    }

    @Test
    void update_ShouldReturnUpdatedPassenger_WhenUpdateIsSuccessful() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(passengerRepository.findPassengerByEmailAndPhoneNumber("test@example.com", "+375331234567"))
                .thenReturn(Optional.empty());
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        Passenger result = passengerService.update(passenger);

        assertEquals(passenger, result);
        verify(passengerRepository).save(passenger);
    }

    @Test
    void update_ShouldThrowException_WhenPassengerAlreadyExists() {
        Passenger tempPassenger = Passenger.builder()
                .id(2L)
                .email("test@example.com")
                .phoneNumber("+375331234567")
                .build();
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(passengerRepository.findPassengerByEmailAndPhoneNumber("test@example.com", "+375331234567"))
                .thenReturn(Optional.of(tempPassenger));

        assertThrows(PassengerAlreadyExistsException.class, () -> passengerService.update(passenger));
        verify(passengerRepository, never()).save(any());
    }
}
