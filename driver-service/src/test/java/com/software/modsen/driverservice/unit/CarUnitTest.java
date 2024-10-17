package com.software.modsen.driverservice.unit;

import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.exception.CarAlreadyExistsException;
import com.software.modsen.driverservice.exception.CarNotExistsException;
import com.software.modsen.driverservice.repository.CarRepository;
import com.software.modsen.driverservice.service.impl.CarServiceImpl;
import com.software.modsen.driverservice.util.CarTestEntities;
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

import static com.software.modsen.driverservice.util.CarTestEntities.CAR_ID;
import static com.software.modsen.driverservice.util.CarTestEntities.CAR_NOT_RESTRICT;
import static com.software.modsen.driverservice.util.CarTestEntities.CAR_NUMBER;
import static com.software.modsen.driverservice.util.CarTestEntities.EXPECTED_LIST_SIZE;
import static com.software.modsen.driverservice.util.CarTestEntities.FIRST_INDEX;
import static com.software.modsen.driverservice.util.CarTestEntities.PAGE_NUMBER;
import static com.software.modsen.driverservice.util.CarTestEntities.PAGE_SIZE;
import static com.software.modsen.driverservice.util.CarTestEntities.SORT_BY_ID;
import static com.software.modsen.driverservice.util.CarTestEntities.WANTED_NUMBER_OF_INVOCATIONS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CarUnitTest {
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        car = CarTestEntities.getTestCar();
    }

    @Test
    void getById_ShouldReturnCar_WhenExists() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car));

        Car result = carService.getById(CAR_ID);

        assertEquals(car, result);
        verify(carRepository).findById(CAR_ID);
    }

    @Test
    void getById_ShouldThrowException_WhenNotExists() {
        when(carRepository.findById(CAR_ID))
                .thenReturn(Optional.empty());

        assertThrows(CarNotExistsException.class, () -> carService.getById(CAR_ID));
        verify(carRepository).findById(CAR_ID);
    }

    @Test
    void getAll_ShouldReturnAllCars_WhenIncludeRestrictedIsFalse() {
        when(carRepository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(SORT_BY_ID))))
                .thenReturn(new PageImpl<>(Collections.singletonList(car)));

        List<Car> result = carService.getAll(PAGE_NUMBER, PAGE_SIZE, SORT_BY_ID, CAR_NOT_RESTRICT);

        assertEquals(EXPECTED_LIST_SIZE, result.size());
        assertEquals(car, result.get(FIRST_INDEX));
        verify(carRepository).findAll(any(Pageable.class));
    }

    @Test
    void save_ShouldReturnCar_WhenSavedSuccessfully() {
        when(carRepository.findCarByNumber(CAR_NUMBER))
                .thenReturn(Optional.empty());
        when(carRepository.save(car))
                .thenReturn(car);

        Car result = carService.save(car);

        assertEquals(car, result);
        verify(carRepository).save(car);
    }

    @Test
    void save_ShouldThrowException_WhenCarAlreadyExists() {
        when(carRepository.findCarByNumber(CAR_NUMBER))
                .thenReturn(Optional.of(car));

        assertThrows(CarAlreadyExistsException.class, () -> carService.save(car));
        verify(carRepository, never()).save(any());
    }

    @Test
    void update_ShouldReturnUpdatedCar_WhenUpdateIsSuccessful() {
        when(carRepository.findById(CAR_ID))
                .thenReturn(Optional.of(car));
        when(carRepository.findCarByNumber(CAR_NUMBER))
                .thenReturn(Optional.empty());
        when(carRepository.save(car))
                .thenReturn(car);

        Car result = carService.update(car);

        assertEquals(car, result);
        verify(carRepository).save(car);
    }

    @Test
    void update_ShouldThrowException_WhenCarAlreadyExists() {
        Car secondCar = CarTestEntities.getSecondTestCar();
        when(carRepository.findById(CAR_ID))
                .thenReturn(Optional.of(car));
        when(carRepository.findCarByNumber(CAR_NUMBER))
                .thenReturn(Optional.of(secondCar));

        assertThrows(CarAlreadyExistsException.class, () -> carService.update(car));
        verify(carRepository, never()).save(any());
    }

    @Test
    void changeRestrictionsStatus_ShouldChangeStatus_WhenCarExists() {
        when(carRepository.findById(CAR_ID))
                .thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);

        Car updatedCar = carService.changeRestrictionsStatus(CAR_ID);

        assertTrue(updatedCar.isRestricted());
        verify(carRepository, times(WANTED_NUMBER_OF_INVOCATIONS)).save(car);
    }

    @Test
    void changeRestrictionsStatus_ShouldThrowException_WhenCarDoesNotExist() {
        when(carRepository.findById(CAR_ID))
                .thenReturn(Optional.empty());

        assertThrows(CarNotExistsException.class, () -> carService.changeRestrictionsStatus(CAR_ID));
        verify(carRepository, never()).save(any());
    }
}
