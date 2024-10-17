package com.software.modsen.driverservice.component.rating;

import com.software.modsen.driverservice.entity.DriverRating;
import com.software.modsen.driverservice.exception.DriverRatingNotExistsException;
import com.software.modsen.driverservice.repository.DriverRatingRepository;
import com.software.modsen.driverservice.service.impl.DriverRatingServiceImpl;
import com.software.modsen.driverservice.util.DriverRatingTestEntities;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.software.modsen.driverservice.util.DriverRatingTestEntities.DRIVER_RATING;
import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_ID;
import static com.software.modsen.driverservice.util.ExceptionMessages.RATING_NOT_EXISTS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
public class DriverRatingComponentTest {

    @Mock
    private DriverRatingRepository driverRatingRepository;

    @InjectMocks
    private DriverRatingServiceImpl driverRatingService;

    private DriverRating driverRating;
    private Exception exception;

    @Given("A driver rating with id {long} exists")
    public void aDriverRatingWithIdExists(Long id) {
        driverRating = DriverRatingTestEntities.getTestDriverRatingForComponent();
        when(driverRatingRepository.findDriverRatingByDriverId(id)).thenReturn(Optional.of(driverRating));
    }

    @When("I request the driver rating with id {long} from getById method")
    public void iRequestTheDriverRatingWithIdFromGetByIdMethod(Long id) {
        try {
            driverRating = driverRatingService.getByDriverId(id);
        } catch (DriverRatingNotExistsException e) {
            exception = e;
        }
    }

    @Then("I should receive the driver rating details with id {long}")
    public void iShouldReceiveTheDriverRatingDetailsWithId(Long id) {
        assertNotNull(driverRating);
        assertEquals(id, driverRating.getDriver().getId());
    }

    @Given("A driver rating exists with id {long} does not exist")
    public void aDriverRatingExistsWithIdDoesNotExist(Long id) {
        when(driverRatingRepository.findById(id)).thenReturn(Optional.empty());
    }

    @Then("I should receive a DriverRatingNotExistsException with id {long}")
    public void iShouldReceiveADriverRatingNotExistsExceptionWithId(Long id) {
        String expected = String.format(RATING_NOT_EXISTS, id);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A driver rating with id {long} does not exist and saved")
    public void aDriverRatingWithIdDoesNotExistAndSaved(Long id) {
        driverRating = DriverRatingTestEntities.getTestDriverRatingForComponent();
        when(driverRatingRepository.findById(id)).thenReturn(Optional.of(driverRating));
        when(driverRatingRepository.save(any(DriverRating.class))).thenReturn(driverRating);
    }

    @When("I save driver rating")
    public void iSaveDriverRating() {
        driverRating = driverRatingRepository.save(driverRating);
    }

    @Then("The new driver rating should be saved successfully")
    public void theNewDriverRatingShouldBeSavedSuccessfully() {
        assertNotNull(driverRating);
        assertEquals(DRIVER_ID, driverRating.getDriver().getId());
        assertEquals(DRIVER_RATING, driverRating.getRating());
        verify(driverRatingRepository).save(any(DriverRating.class));
    }

    @Given("A driver rating with id {long} exists and saved")
    public void aDriverRatingWithIdExistsAndSaved(Long id) {
        driverRating = DriverRatingTestEntities.getTestDriverRatingForComponent();
        when(driverRatingRepository.findById(id)).thenReturn(Optional.of(driverRating));
        when(driverRatingRepository.save(driverRating)).thenReturn(driverRating);
    }

    @Then("The existing driver rating should be saved successfully")
    public void theExistingDriverRatingShouldBeSavedSuccessfully() {
        assertNotNull(driverRating);
        assertEquals(DRIVER_ID, driverRating.getDriver().getId());
        assertEquals(DRIVER_RATING, driverRating.getRating());
        verify(driverRatingRepository).save(driverRating);
    }
}
