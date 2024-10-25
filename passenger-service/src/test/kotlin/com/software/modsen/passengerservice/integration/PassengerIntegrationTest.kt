package com.software.modsen.passengerservice.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.software.modsen.passengerservice.repository.PassengerRepository
import com.software.modsen.passengerservice.util.TestEntities
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

@SpringBootTest
@AutoConfigureMockMvc
class PassengerIntegrationTest : PostgresTestContainerSetup() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var passengerRepository: PassengerRepository

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper()
        passengerRepository.deleteAll()
    }

    @Test
    fun `test get all passengers`() {
        val firstPassenger = TestEntities.getPassengerForIT()
        val secondPassenger = TestEntities.getSecondPassengerForIT()
        passengerRepository.save(firstPassenger)
        passengerRepository.save(secondPassenger)

        mockMvc.perform(get(TestEntities.PASSENGER_BASE_URL))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.items").isArray)
            .andExpect(jsonPath("$.items.length()").value(TestEntities.EXPECTED_PASSENGER_LIST_SIZE))
            .andExpect(jsonPath("$.items[0].email").value(TestEntities.PASSENGER_EMAIL))
            .andExpect(jsonPath("$.items[1].email").value(TestEntities.SECOND_PASSENGER_EMAIL))
    }

    @Test
    fun `test get passenger by id`() {
        val passenger = TestEntities.getPassengerForIT()
        passengerRepository.save(passenger)

        mockMvc.perform(get("${TestEntities.PASSENGER_BASE_URL}/{id}", passenger.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.email").value(TestEntities.PASSENGER_EMAIL))
    }

    @Test
    fun `test create passenger`() {
        val request = TestEntities.getPassengerCreateRequestForIT()
        val jsonRequest = objectMapper.writeValueAsString(request)

        mockMvc.perform(post(TestEntities.PASSENGER_BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest))
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        val passengers = passengerRepository.findAll()
        assertThat(passengers).hasSize(TestEntities.EXPECTED_LIST_SIZE)
        assertThat(passengers[0].email).isEqualTo(TestEntities.PASSENGER_EMAIL)
        assertThat(passengers[0].phoneNumber).isEqualTo(TestEntities.PASSENGER_PHONE_NUMBER)
    }

    @Test
    fun `test update passenger`() {
        val passenger = TestEntities.getPassengerForIT()
        passengerRepository.save(passenger)

        val request = TestEntities.getPassengerUpdateRequestForIT(passenger.id)
        val jsonRequest = objectMapper.writeValueAsString(request)

        mockMvc.perform(put(TestEntities.PASSENGER_BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        val updatedPassenger = passengerRepository.findById(passenger.id!!).orElseThrow()
        assertThat(updatedPassenger.email).isEqualTo(TestEntities.SECOND_PASSENGER_EMAIL)
    }

    @Test
    fun `test change restrictions status`() {
        val passenger = TestEntities.getPassengerForIT()
        passengerRepository.save(passenger)

        val request = TestEntities.getTestPassengerChangeStatusRequest(passenger.id)
        val jsonRequest = objectMapper.writeValueAsString(request)

        mockMvc.perform(put("${TestEntities.PASSENGER_BASE_URL}/status")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        val updatedPassenger = passengerRepository.findById(passenger.id!!).orElseThrow()
        assertThat(updatedPassenger.restricted).isTrue
    }
}