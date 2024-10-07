package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.entity.Driver;
import com.software.modsen.driverservice.exception.DriverAlreadyExistsException;
import com.software.modsen.driverservice.exception.DriverNotExistsException;
import com.software.modsen.driverservice.repository.DriverRepository;
import com.software.modsen.driverservice.service.impl.DriverServiceImpl;
import com.software.modsen.driverservice.util.DriverTestEntities;
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

import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_EMAIL;
import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_ID;
import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_NOT_RESTRICT;
import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_PHONE_NUMBER;
import static com.software.modsen.driverservice.util.DriverTestEntities.EXPECTED_LIST_SIZE;
import static com.software.modsen.driverservice.util.DriverTestEntities.FIRST_INDEX;
import static com.software.modsen.driverservice.util.DriverTestEntities.PAGE_NUMBER;
import static com.software.modsen.driverservice.util.DriverTestEntities.PAGE_SIZE;
import static com.software.modsen.driverservice.util.DriverTestEntities.SORT_BY_ID;
import static com.software.modsen.driverservice.util.DriverTestEntities.WANTED_NUMBER_OF_INVOCATIONS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DriverServiceTest {
    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarService carService;

    @InjectMocks
    private DriverServiceImpl driverService;

    private Driver driver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        driver = DriverTestEntities.getTestDriver();
    }

    @Test
    void getById_ShouldReturnDriver_WhenExists() {
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driver));

        Driver result = driverService.getById(DRIVER_ID);

        assertEquals(driver, result);
        verify(driverRepository).findById(DRIVER_ID);
    }

    @Test
    void getById_ShouldThrowException_WhenNotExists() {
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.empty());

        assertThrows(DriverNotExistsException.class, () -> driverService.getById(DRIVER_ID));
        verify(driverRepository).findById(DRIVER_ID);
    }

    @Test
    void getAll_ShouldReturnAllDrivers_WhenIncludeRestrictedIsFalse() {
        when(driverRepository.findAll(PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by(SORT_BY_ID))))
                .thenReturn(new PageImpl<>(Collections.singletonList(driver)));

        List<Driver> result = driverService.getAll(PAGE_NUMBER, PAGE_SIZE, SORT_BY_ID, DRIVER_NOT_RESTRICT);

        assertEquals(EXPECTED_LIST_SIZE, result.size());
        assertEquals(driver, result.get(FIRST_INDEX));
        verify(driverRepository).findAll(any(Pageable.class));
    }

    @Test
    void save_ShouldReturnDriver_WhenSavedSuccessfully() {
        when(driverRepository.findDriverByEmailAndPhoneNumber(DRIVER_EMAIL, DRIVER_PHONE_NUMBER))
                .thenReturn(Optional.empty());
        when(carService.getById(driver.getCar().getId())).thenReturn(driver.getCar());
        when(driverRepository.save(driver)).thenReturn(driver);

        Driver result = driverService.save(driver);

        assertEquals(driver, result);
        verify(driverRepository).save(driver);
    }

    @Test
    void save_ShouldThrowException_WhenDriverAlreadyExists() {
        when(driverRepository.findDriverByEmailAndPhoneNumber(DRIVER_EMAIL, DRIVER_PHONE_NUMBER))
                .thenReturn(Optional.of(driver));

        assertThrows(DriverAlreadyExistsException.class, () -> driverService.save(driver));
        verify(driverRepository, never()).save(any());
    }

    @Test
    void update_ShouldReturnUpdatedDriver_WhenUpdateIsSuccessful() {
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driver));
        when(driverRepository.findDriverByEmailAndPhoneNumber(DRIVER_EMAIL, DRIVER_PHONE_NUMBER))
                .thenReturn(Optional.empty());
        when(carService.getById(driver.getCar().getId())).thenReturn(driver.getCar());
        when(driverRepository.save(driver)).thenReturn(driver);

        Driver result = driverService.update(driver);

        assertEquals(driver, result);
        verify(driverRepository).save(driver);
    }

    @Test
    void update_ShouldThrowException_WhenDriverAlreadyExists() {
        Driver secondDriver = DriverTestEntities.getSecondTestDriver();
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driver));
        when(driverRepository.findDriverByEmailAndPhoneNumber(DRIVER_EMAIL, DRIVER_PHONE_NUMBER))
                .thenReturn(Optional.of(secondDriver));

        assertThrows(DriverAlreadyExistsException.class, () -> driverService.update(driver));
        verify(driverRepository, never()).save(any());
    }

    @Test
    void changeRestrictionsStatus_ShouldChangeStatus_WhenDriverExists() {
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driver));
        when(driverRepository.save(driver)).thenReturn(driver);

        Driver updatedDriver = driverService.changeRestrictionsStatus(DRIVER_ID);

        assertTrue(updatedDriver.isRestricted());
        verify(driverRepository, times(WANTED_NUMBER_OF_INVOCATIONS)).save(driver);
    }

    @Test
    void changeRestrictionsStatus_ShouldThrowException_WhenDriverDoesNotExist() {
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.empty());

        assertThrows(DriverNotExistsException.class, () -> driverService.changeRestrictionsStatus(DRIVER_ID));
        verify(driverRepository, never()).save(any());
    }

    @Test
    void changeBusyStatus_ShouldChangeBusyStatus_WhenDriverExists() {
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driver));
        when(driverRepository.save(driver)).thenReturn(driver);

        Driver updatedDriver = driverService.changeBusyStatus(DRIVER_ID);

        assertTrue(updatedDriver.isBusy());
        verify(driverRepository, times(WANTED_NUMBER_OF_INVOCATIONS)).save(driver);
    }

    @Test
    void changeBusyStatus_ShouldThrowException_WhenDriverDoesNotExist() {
        when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.empty());

        assertThrows(DriverNotExistsException.class, () -> driverService.changeBusyStatus(DRIVER_ID));
        verify(driverRepository, never()).save(any());
    }
}
