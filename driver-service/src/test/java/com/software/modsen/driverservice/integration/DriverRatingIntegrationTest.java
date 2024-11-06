package com.software.modsen.driverservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.driverservice.dto.request.DriverRatingRequest;
import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.entity.Driver;
import com.software.modsen.driverservice.entity.DriverRating;
import com.software.modsen.driverservice.repository.CarRepository;
import com.software.modsen.driverservice.repository.DriverRatingRepository;
import com.software.modsen.driverservice.repository.DriverRepository;
import com.software.modsen.driverservice.util.CarTestEntities;
import com.software.modsen.driverservice.util.DriverRatingTestEntities;
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

import static com.software.modsen.driverservice.util.DriverRatingTestEntities.DRIVER_RATING;
import static com.software.modsen.driverservice.util.DriverRatingTestEntities.DRIVER_RATING_BASE_URL;
import static com.software.modsen.driverservice.util.DriverRatingTestEntities.FIRST_INDEX;
import static com.software.modsen.driverservice.util.DriverTestEntities.DRIVER_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DriverRatingIntegrationTest extends PostgresTestContainerSetup {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverRatingRepository driverRatingRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CarRepository carRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        driverRatingRepository.deleteAll();
        driverRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void testGetDriverRatingById() {
        Car car = CarTestEntities.getTestCar();
        carRepository.save(car);

        Driver driver = DriverTestEntities.getTestDriver();
        driverRepository.save(driver);

        DriverRating driverRating = DriverRatingTestEntities.getTestDriverRating();
        driverRating.setDriver(driver);
        driverRatingRepository.save(driverRating);

        mockMvc.perform(get(DRIVER_RATING_BASE_URL + "/{id}", driverRating.getDriver().getId())
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_admin"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.driverRating").value(DRIVER_RATING));
    }

    @Test
    @SneakyThrows
    public void testSaveDriverRating() {
        Car car = CarTestEntities.getCarForIT();
        carRepository.save(car);

        Driver driver = DriverTestEntities.getDriverForIT();
        driverRepository.save(driver);

        DriverRatingRequest request = DriverRatingTestEntities.getTestDriverRatingRequest(driver.getId());
        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(DRIVER_RATING_BASE_URL).with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_passenger")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<DriverRating> driverRatingList = driverRatingRepository.findAll();
        assertThat(driverRatingList.get(FIRST_INDEX).getRating()).isEqualTo(DRIVER_RATING);
        assertThat(driverRatingList.get(FIRST_INDEX).getDriver().getEmail()).isEqualTo(DRIVER_EMAIL);
    }
}
