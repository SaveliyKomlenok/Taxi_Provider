package com.software.modsen.passengerservice.contract;

import com.software.modsen.passengerservice.controller.PassengerController;
import com.software.modsen.passengerservice.service.PassengerService;
import com.software.modsen.passengerservice.util.TestEntities;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.software.modsen.passengerservice.util.TestEntities.PASSENGER_ID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class PassengerContractTest {
    @Autowired
    private PassengerController passengerController;

    @MockBean
    private PassengerService passengerService;

    @BeforeEach
    @SneakyThrows
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(passengerController);

        Mockito.when(passengerService.getById(PASSENGER_ID))
                .thenReturn(TestEntities.getTestPassenger());
    }
}
