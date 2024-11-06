package com.software.modsen.driverservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.driverservice.dto.request.CarChangeStatusRequest;
import com.software.modsen.driverservice.dto.request.CarCreateRequest;
import com.software.modsen.driverservice.dto.request.CarUpdateRequest;
import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.repository.CarRepository;
import com.software.modsen.driverservice.util.CarTestEntities;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.software.modsen.driverservice.util.CarTestEntities.CAR_BASE_URL;
import static com.software.modsen.driverservice.util.CarTestEntities.CAR_KIND;
import static com.software.modsen.driverservice.util.CarTestEntities.CAR_NUMBER;
import static com.software.modsen.driverservice.util.CarTestEntities.EXPECTED_LIST_SIZE;
import static com.software.modsen.driverservice.util.CarTestEntities.FIRST_INDEX;
import static com.software.modsen.driverservice.util.CarTestEntities.SECOND_CAR_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CarIntegrationTest extends PostgresTestContainerSetup {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        carRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void testGetAllCars() {
        Car firstCar = CarTestEntities.getCarForIT();
        Car secondCar = CarTestEntities.getSecondCarForIT();
        carRepository.save(firstCar);
        carRepository.save(secondCar);

        mockMvc.perform(get(CAR_BASE_URL).with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority("ROLE_admin"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.carResponseList").isArray())
                .andExpect(jsonPath("$.carResponseList.length()").value(2))
                .andExpect(jsonPath("$.carResponseList[0].number").value(CAR_NUMBER))
                .andExpect(jsonPath("$.carResponseList[1].number").value(SECOND_CAR_NUMBER));
    }

    @Test
    @SneakyThrows
    public void testGetCarById() {
        Car car = CarTestEntities.getCarForIT();
        carRepository.save(car);

        mockMvc.perform(get(CAR_BASE_URL + "/{id}", car.getId()).with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority("ROLE_admin"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.kind").value(CAR_KIND));
    }

    @Test
    @SneakyThrows
    public void testCreateCar() {
        CarCreateRequest request = CarTestEntities.getCarCreateRequestForIT();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(CAR_BASE_URL).with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_admin")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Car> cars = carRepository.findAll();
        assertThat(cars).hasSize(EXPECTED_LIST_SIZE);
        assertThat(cars.get(FIRST_INDEX).getKind()).isEqualTo(CAR_KIND);
    }

    @Test
    @SneakyThrows
    public void testUpdateCar() {
        Car car = CarTestEntities.getCarForIT();
        carRepository.save(car);

        CarUpdateRequest request = CarTestEntities.getCarUpdateRequestForIT();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(CAR_BASE_URL).with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_admin")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Car updatedCar = carRepository.findById(car.getId()).orElseThrow();
        assertThat(updatedCar.getNumber()).isEqualTo(SECOND_CAR_NUMBER);
    }

    @Test
    @SneakyThrows
    public void testChangeRestrictionsStatus() {
        Car car = CarTestEntities.getCarForIT();
        carRepository.save(car);

        CarChangeStatusRequest request = CarTestEntities.getCarChangeStatusRequest(car.getId());
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(CAR_BASE_URL + "/restrict").with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_admin")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        Car updatedCar = carRepository.findById(car.getId()).orElseThrow();
        assertThat(updatedCar.isRestricted()).isTrue();
    }
}