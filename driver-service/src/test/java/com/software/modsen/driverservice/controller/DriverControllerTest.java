package com.software.modsen.driverservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.driverservice.dto.request.DriverCreateRequest;
import com.software.modsen.driverservice.dto.request.DriverUpdateRequest;
import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.entity.Driver;
import com.software.modsen.driverservice.repository.CarRepository;
import com.software.modsen.driverservice.repository.DriverRepository;
import com.software.modsen.driverservice.util.CarTestEntities;
import com.software.modsen.driverservice.util.DriverTestEntities;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_BASE_URL;
import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_EMAIL;
import static com.software.modsen.driverservice.util.DriverTestEntities.EXPECTED_LIST_SIZE;
import static com.software.modsen.driverservice.util.DriverTestEntities.FIRST_INDEX;
import static com.software.modsen.driverservice.util.DriverTestEntities.SECOND_DRIVER_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DriverControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private DriverRepository driverRepository;

    private ObjectMapper objectMapper;

    @BeforeAll
    public static void setUpPostgresDB() {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("driver_test_db")
                .withUsername("postgres")
                .withPassword("root");
        postgreSQLContainer.start();

        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        driverRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void testGetAllDrivers() {
        Driver firstDriver = DriverTestEntities.getDriverForIT();
        Driver secondDriver = DriverTestEntities.getSecondDriverForIT();
        driverRepository.save(firstDriver);
        driverRepository.save(secondDriver);

        mockMvc.perform(get(DRIVER_BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.driverResponseList").isArray())
                .andExpect(jsonPath("$.driverResponseList.length()").value(2))
                .andExpect(jsonPath("$.driverResponseList[0].email").value(DRIVER_EMAIL))
                .andExpect(jsonPath("$.driverResponseList[1].email").value(SECOND_DRIVER_EMAIL));
    }

    @Test
    @SneakyThrows
    public void testGetDriverById() {
        Driver driver = DriverTestEntities.getDriverForIT();
        driverRepository.save(driver);

        mockMvc.perform(get(DRIVER_BASE_URL + "/{id}", driver.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(DRIVER_EMAIL));
    }

    @Test
    @SneakyThrows
    public void testCreateDriver() {
        Car car = CarTestEntities.getCarForIT();
        carRepository.save(car);

        DriverCreateRequest request = DriverTestEntities.getDriverCreateRequestForIT();
        request.setCar(car.getId());
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(DRIVER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Driver> drivers = driverRepository.findAll();
        assertThat(drivers).hasSize(EXPECTED_LIST_SIZE);
        assertThat(drivers.get(FIRST_INDEX).getEmail()).isEqualTo(DRIVER_EMAIL);
    }

    @Test
    @SneakyThrows
    public void testUpdateDriver() {
        Car car = CarTestEntities.getCarForIT();
        carRepository.save(car);

        Driver driver = DriverTestEntities.getDriverForIT();
        driverRepository.save(driver);

        DriverUpdateRequest request = DriverTestEntities.getDriverUpdateRequestForIT();
        request.setId(driver.getId());
        request.setCar(car.getId());
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(DRIVER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Driver updatedDriver = driverRepository.findById(driver.getId()).orElseThrow();
        assertThat(updatedDriver.getEmail()).isEqualTo(SECOND_DRIVER_EMAIL);
    }

    @Test
    @SneakyThrows
    public void testChangeRestrictionsStatus() {
        Driver driver = DriverTestEntities.getDriverForIT();
        driverRepository.save(driver);

        mockMvc.perform(put(DRIVER_BASE_URL + "/restrict/{id}", driver.getId()))
                .andExpect(status().isOk());

        Driver updatedDriver = driverRepository.findById(driver.getId()).orElseThrow();
        assertThat(updatedDriver.isRestricted()).isTrue();
    }

    @Test
    @SneakyThrows
    public void testChangeBusyStatus() {
        Driver driver = DriverTestEntities.getDriverForIT();
        driverRepository.save(driver);

        mockMvc.perform(put(DRIVER_BASE_URL + "/busy/{id}", driver.getId()))
                .andExpect(status().isOk());

        Driver updatedDriver = driverRepository.findById(driver.getId()).orElseThrow();
        assertThat(updatedDriver.isBusy()).isTrue();
    }
}
