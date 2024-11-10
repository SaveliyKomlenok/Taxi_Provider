package com.software.modsen.driverservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.driverservice.dto.request.DriverChangeStatusRequest;
import com.software.modsen.driverservice.dto.request.DriverCreateRequest;
import com.software.modsen.driverservice.dto.request.DriverUpdateRequest;
import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.entity.Driver;
import com.software.modsen.driverservice.repository.CarRepository;
import com.software.modsen.driverservice.repository.DriverRepository;
import com.software.modsen.driverservice.util.CarTestEntities;
import com.software.modsen.driverservice.util.DriverTestEntities;
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

import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_BASE_URL;
import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_EMAIL;
import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_ID;
import static com.software.modsen.driverservice.util.DriverTestEntities.EXPECTED_LIST_SIZE;
import static com.software.modsen.driverservice.util.DriverTestEntities.FIRST_INDEX;
import static com.software.modsen.driverservice.util.DriverTestEntities.SECOND_DRIVER_EMAIL;
import static com.software.modsen.driverservice.util.DriverTestEntities.SECOND_DRIVER_PHONE_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DriverIntegrationTest extends PostgresTestContainerSetup {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private DriverRepository driverRepository;

    private ObjectMapper objectMapper;

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

        mockMvc.perform(get(DRIVER_BASE_URL).with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_admin"))))
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
        mockMvc.perform(get(DRIVER_BASE_URL + "/" + DRIVER_ID).with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_driver"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(DRIVER_EMAIL));
    }

    @Test
    @SneakyThrows
    public void testCreateDriver() {
        DriverCreateRequest request = DriverTestEntities.getDriverCreateRequestForIT();
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(DRIVER_BASE_URL).with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_realm-admin")))
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
        request.setCar(car.getId());
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(DRIVER_BASE_URL).with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_driver")).jwt(jwt -> jwt.claim("userId", String.valueOf(driver.getId()))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Driver updatedDriver = driverRepository.findById(driver.getId()).orElseThrow();
        assertThat(updatedDriver.getPhoneNumber()).isEqualTo(SECOND_DRIVER_PHONE_NUMBER);
    }

    @Test
    @SneakyThrows
    public void testChangeRestrictionsStatus() {
        Driver driver = DriverTestEntities.getDriverForIT();
        driverRepository.save(driver);

        DriverChangeStatusRequest request = DriverTestEntities.getDriverChangeStatusRequest(driver.getId());
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(DRIVER_BASE_URL + "/restrict").with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_admin")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        Driver updatedDriver = driverRepository.findById(driver.getId()).orElseThrow();
        assertThat(updatedDriver.isRestricted()).isTrue();
    }

    @Test
    @SneakyThrows
    public void testChangeBusyStatus() {
        Driver driver = DriverTestEntities.getDriverForIT();
        driverRepository.save(driver);

        DriverChangeStatusRequest request = DriverTestEntities.getDriverChangeStatusRequest(driver.getId());
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(DRIVER_BASE_URL + "/busy").with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_admin")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        Driver updatedDriver = driverRepository.findById(driver.getId()).orElseThrow();
        assertThat(updatedDriver.isBusy()).isTrue();
    }
}
