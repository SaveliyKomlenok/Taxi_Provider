package com.software.modsen.passengerservice.contract

import com.software.modsen.passengerservice.controller.PassengerController
import com.software.modsen.passengerservice.service.PassengerService
import com.software.modsen.passengerservice.util.TestEntities
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import com.software.modsen.passengerservice.util.TestEntities.PASSENGER_ID
import org.mockito.Mockito.mock

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
abstract class PassengerContractTest {

    @Autowired
    lateinit var passengerController: PassengerController

    private var passengerService: PassengerService = mock(PassengerService::class.java)

    @BeforeEach
    fun setup() {
        RestAssuredMockMvc.standaloneSetup(passengerController)

        Mockito.`when`(passengerService.getById(PASSENGER_ID))
            .thenReturn(TestEntities.getTestPassenger())
    }
}