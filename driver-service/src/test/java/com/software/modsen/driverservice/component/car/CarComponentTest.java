package com.software.modsen.driverservice.component.car;

import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.exception.CarAlreadyExistsException;
import com.software.modsen.driverservice.exception.CarNotExistsException;
import com.software.modsen.driverservice.repository.CarRepository;
import com.software.modsen.driverservice.service.impl.CarServiceImpl;
import com.software.modsen.driverservice.util.CarTestEntities;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.software.modsen.driverservice.util.ExceptionMessages.CAR_ALREADY_EXISTS;
import static com.software.modsen.driverservice.util.ExceptionMessages.CAR_NOT_EXISTS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@CucumberContextConfiguration
public class CarComponentTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;
    private Exception exception;

    @Given("A car with id {long} exists")
    public void aCarWithIdExists(Long id) {
        car = CarTestEntities.getTestCar();
        when(carRepository.findById(id)).thenReturn(Optional.of(car));
    }

    @When("I request the car with id {long} from getById method")
    public void iRequestTheCarWithId(Long id) {
        try {
            car = carService.getById(id);
        } catch (CarNotExistsException e) {
            exception = e;
        }
    }

    @Then("I should receive the car details with id {long}")
    public void iShouldReceiveTheCarDetails(Long id) {
        assertNotNull(car);
        assertEquals(id, car.getId());
    }

    @Given("A car exists with id {long} does not exist")
    public void aCarExistsWithIdDoesNotExist(Long id) {
        when(carRepository.findById(id)).thenReturn(Optional.empty());
    }

    @Then("I should receive a CarNotExistsException with id {long}")
    public void iShouldReceiveACarNotExistsExceptionWithId(Long id) {
        String expected = String.format(CAR_NOT_EXISTS, id);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A car with number {string} does not exist")
    public void aCarWithNumberDoesNotExist(String number) {
        car = CarTestEntities.getTestCar();
        when(carRepository.findCarByNumber(number)).thenReturn(Optional.empty());
        when(carRepository.save(car)).thenReturn(car);
    }

    @When("I save the car")
    public void iSaveTheCar() {
        try {
            car = carService.save(car);
        } catch (CarAlreadyExistsException e) {
            exception = e;
        }
    }

    @Then("The car should be saved successfully")
    public void theCarShouldBeSavedSuccessfully() {
        Car expected = CarTestEntities.getTestCar();
        assertEquals(car, expected);
        verify(carRepository).save(car);
    }

    @Given("A car with number {string} already exists")
    public void aCarWithNumberAlreadyExists(String number) {
        car = CarTestEntities.getTestCar();
        when(carRepository.findCarByNumber(number)).thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);
    }

    @Then("I should receive a CarAlreadyExistsException")
    public void iShouldReceiveACarAlreadyExistsException() {
        String expected = String.format(CAR_ALREADY_EXISTS);
        String actual = exception.getMessage();

        assertThat(actual).isEqualTo(expected);
    }

    @Given("A car with id {long} exists and saved")
    public void aCarWithIdExistsAndSaved(Long id) {
        car = CarTestEntities.getTestCar();
        when(carRepository.findById(id)).thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);
    }

    @When("I update the car")
    public void iUpdateTheCar() {
        try {
            car = carService.update(car);
        } catch (CarAlreadyExistsException e) {
            exception = e;
        }
    }

    @Then("The car should be updated successfully")
    public void theCarShouldBeUpdatedSuccessfully() {
        Car expected = CarTestEntities.getTestCar();
        assertEquals(car, expected);
        verify(carRepository).save(car);
    }

    @Given("A car with number {string} already exists for update")
    public void aCarWithNumberAlreadyExistsForUpdate(String number) {
        Car secondCar = CarTestEntities.getSecondTestCar();
        when(carRepository.findCarByNumber(number)).thenReturn(Optional.of(secondCar));
        when(carRepository.save(car)).thenReturn(car);
    }

    @When("I change the restrictions status of the car with id {long}")
    public void iChangeTheRestrictionsStatusOfTheCar(Long id) {
        try {
            car = carService.changeRestrictionsStatus(id);
        } catch (CarNotExistsException e) {
            exception = e;
        }
    }

    @Then("The car restriction status should be changed")
    public void theCarRestrictionsStatusShouldBeChanged() {
        assertNotNull(car);
        assertTrue(car.isRestricted());
        verify(carRepository).save(car);
    }
}
