package com.software.modsen.driverservice.service;

import com.software.modsen.driverservice.entity.Driver;
import com.software.modsen.driverservice.entity.DriverRating;
import com.software.modsen.driverservice.exception.DriverRatingNotExistsException;
import com.software.modsen.driverservice.repository.DriverRatingRepository;
import com.software.modsen.driverservice.service.impl.DriverRatingServiceImpl;
import com.software.modsen.driverservice.util.DriverRatingTestEntities;
import com.software.modsen.driverservice.util.DriverTestEntities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.software.modsen.driverservice.util.DriverRatingTestEntities.DRIVER_RATING;
import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_EMAIL;
import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DriverRatingServiceTest {
    @Mock
    private DriverRatingRepository driverRatingRepository;

    @Mock
    private DriverService driverService;

    @InjectMocks
    private DriverRatingServiceImpl driverRatingService;

    private Driver driver;
    private DriverRating driverRating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        driver = DriverTestEntities.getTestDriver();
        driverRating = DriverRatingTestEntities.getTestDriverRating();
    }

    @Test
    void getByDriverId_ShouldReturnDriverRating_WhenExists() {
        when(driverRatingRepository.findDriverRatingByDriverId(DRIVER_ID)).thenReturn(Optional.of(driverRating));

        DriverRating result = driverRatingService.getByDriverId(DRIVER_ID);

        assertEquals(driverRating, result);
        verify(driverRatingRepository).findDriverRatingByDriverId(DRIVER_ID);
    }

    @Test
    void getByDriverId_ShouldThrowException_WhenNotExists() {
        when(driverRatingRepository.findDriverRatingByDriverId(DRIVER_ID)).thenReturn(Optional.empty());

        assertThrows(DriverRatingNotExistsException.class, () -> driverRatingService.getByDriverId(DRIVER_ID));
        verify(driverRatingRepository).findDriverRatingByDriverId(DRIVER_ID);
    }

    @Test
    void save_ShouldReturnSavedDriverRating_WhenSavedSuccessfully() {
        when(driverRatingRepository.findDriverRatingByDriverId(DRIVER_ID)).thenReturn(Optional.empty());
        when(driverService.getById(DRIVER_ID)).thenReturn(driver);
        when(driverRatingRepository.save(any(DriverRating.class))).thenReturn(driverRating);

        DriverRating result = driverRatingService.save(driverRating);

        assertNotNull(result);
        assertEquals(driverRating.getRating(), result.getRating());
        verify(driverRatingRepository).save(any(DriverRating.class));
    }

    @Test
    void save_ShouldUpdateExistingRating_WhenDriverRatingExists() {
        when(driverRatingRepository.findDriverRatingByDriverId(DRIVER_ID))
                .thenReturn(Optional.of(driverRating));
        when(driverService.getById(DRIVER_ID)).thenReturn(driver);
        when(driverRatingRepository.save(driverRating)).thenReturn(driverRating);

        DriverRating savedRating = driverRatingRepository.save(driverRating);

        assertNotNull(savedRating);
        assertEquals(DRIVER_EMAIL, savedRating.getDriver().getEmail());
        assertEquals(DRIVER_RATING, savedRating.getRating());
        verify(driverRatingRepository).save(driverRating);
    }
}
