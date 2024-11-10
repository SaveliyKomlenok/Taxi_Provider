package com.software.modsen.driverservice.component.driver;

import com.software.modsen.driverservice.entity.Driver;
import com.software.modsen.driverservice.exception.DriverAlreadyExistsException;
import com.software.modsen.driverservice.exception.DriverNotExistsException;
import com.software.modsen.driverservice.repository.DriverRepository;
import com.software.modsen.driverservice.service.CarService;
import com.software.modsen.driverservice.service.impl.DriverServiceImpl;
import com.software.modsen.driverservice.util.DriverTestEntities;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.software.modsen.driverservice.util.ExceptionMessages.DRIVER_ALREADY_EXISTS;
import static com.software.modsen.driverservice.util.ExceptionMessages.DRIVER_NOT_EXISTS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
public class DriverComponentTest {
    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarService carService;

    @InjectMocks
    private DriverServiceImpl driverService;

    private Driver driver;
    private Exception exception;

    @Given("A driver with id {long} exists")
    public void aDriverWithIdExists(Long id) {
        driver = DriverTestEntities.getTestDriverForComponent();
        when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
    }

    @When("I request the driver with id {long} from getById method")
    public void iRequestTheDriverWithId(Long id) {
        try {
            driver = driverService.getById(id);
        } catch (DriverNotExistsException e) {
            exception = e;
        }
    }

    @Then("I should receive the driver details with id {long}")
    public void iShouldReceiveTheDriverDetails(Long id) {
        assertNotNull(driver);
        assertEquals(id, driver.getId());
    }

    @Given("A driver exists with id {long} does not exist")
    public void aDriverExistsWithIdDoesNotExist(Long id) {
        when(driverRepository.findById(id)).thenReturn(Optional.empty());
    }

    @Then("I should receive a DriverNotExistsException with id {long}")
    public void iShouldReceiveADriverNotExistsExceptionWithId(Long id) {
        String expected = String.format(DRIVER_NOT_EXISTS, id);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A driver with email {string} and phone number {string} does not exist")
    public void aDriverWithEmailAndPhoneNumberDoesNotExist(String email, String phoneNumber) {
        driver = DriverTestEntities.getTestDriverForComponent();
        when(driverRepository.findDriverByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(Optional.empty());
        when(carService.getById(driver.getCar().getId())).thenReturn(driver.getCar());
        when(driverRepository.save(driver)).thenReturn(driver);
    }

    @When("I save the driver")
    public void iSaveTheDriver() {
        try {
            driver = driverService.save(driver);
        } catch (DriverAlreadyExistsException e) {
            exception = e;
        }
    }

    @Then("The driver should be saved successfully")
    public void theDriverShouldBeSavedSuccessfully() {
        Driver expected = DriverTestEntities.getTestDriverForComponent();
        assertEquals(driver, expected);
        verify(driverRepository).save(driver);
    }

    @Given("A driver with email {string} and phone number {string} already exists")
    public void aDriverWithEmailAndPhoneNumberAlreadyExists(String email, String phoneNumber) {
        driver = DriverTestEntities.getTestDriverForComponent();
        when(driverRepository.findDriverByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(Optional.of(driver));
        when(carService.getById(driver.getCar().getId())).thenReturn(driver.getCar());
        when(driverRepository.save(driver)).thenReturn(driver);
    }

    @Then("I should receive a DriverAlreadyExistsException")
    public void iShouldReceiveADriverAlreadyExistsException() {
        String expected = String.format(DRIVER_ALREADY_EXISTS);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A driver with id {long} exists and saved")
    public void aDriverWithIdExistsAndSaved(Long id) {
        driver = DriverTestEntities.getTestDriverForComponent();
        when(driverRepository.findById(id)).thenReturn(Optional.of(driver));
        when(carService.getById(driver.getCar().getId())).thenReturn(driver.getCar());
        when(driverRepository.save(driver)).thenReturn(driver);
    }

    @When("I update the driver with id {long}")
    public void iUpdateTheDriver(Long id) {
        try {
            driver = driverService.update(id, driver);
        } catch (DriverAlreadyExistsException e) {
            exception = e;
        }
    }

    @Then("The driver should be updated successfully")
    public void theDriverShouldBeUpdatedSuccessfully() {
        Driver expected = DriverTestEntities.getTestDriverForComponent();
        assertEquals(driver, expected);
        verify(driverRepository).save(driver);
    }

    @Given("A driver with email {string} and phone number {string} already exists for update")
    public void aDriverWithEmailAndPhoneNumberAlreadyExistsForUpdate(String email, String phoneNumber) {
        Driver secondDriver = DriverTestEntities.getSecondTestDriver();
        when(driverRepository.findDriverByEmailAndPhoneNumber(email, phoneNumber)).thenReturn(Optional.of(secondDriver));
        when(carService.getById(driver.getCar().getId())).thenReturn(driver.getCar());
        when(driverRepository.save(driver)).thenReturn(driver);
    }

    @When("I change the restrictions status of the driver with id {long}")
    public void iChangeTheRestrictionsStatusOfTheDriver(Long id) {
        try {
            driver = driverService.changeRestrictionsStatus(DriverTestEntities.getDriverChangeStatusRequest(id));
        } catch (DriverNotExistsException e) {
            exception = e;
        }
    }

    @Then("The driver restriction status should be changed")
    public void theDriverRestrictionsStatusShouldBeChanged() {
        assertNotNull(driver);
        assertTrue(driver.isRestricted());
        verify(driverRepository).save(driver);
    }

    @When("I change the busy status of the driver with id {long}")
    public void iChangeTheBusyStatusOfTheDriverWithId(Long id) {
        try {
            driver = driverService.changeBusyStatus(DriverTestEntities.getDriverChangeStatusRequest(id));
        } catch (DriverNotExistsException e) {
            exception = e;
        }
    }

    @Then("The driver busy status should be changed")
    public void theDriverBusyStatusShouldBeChanged() {
        assertNotNull(driver);
        assertTrue(driver.isBusy());
        verify(driverRepository).save(driver);
    }
}
