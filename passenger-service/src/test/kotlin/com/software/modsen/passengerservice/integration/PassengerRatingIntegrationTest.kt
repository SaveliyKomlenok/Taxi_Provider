package com.software.modsen.passengerservice.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.software.modsen.passengerservice.repository.PassengerRatingRepository
import com.software.modsen.passengerservice.repository.PassengerRepository
import com.software.modsen.passengerservice.util.TestEntities
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class PassengerRatingIntegrationTest : PostgresTestContainerSetup() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var passengerRatingRepository: PassengerRatingRepository

    @Autowired
    lateinit var passengerRepository: PassengerRepository

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper()
        passengerRatingRepository.deleteAll()
    }

    @Test
    fun `test get passenger rating by id`() {
        val passenger = TestEntities.getPassengerForIT()
        passengerRepository.save(passenger)

        val passengerRating = TestEntities.getPassengerRatingForIT(passenger).apply {
            this.passenger = passenger
        }
        passengerRatingRepository.save(passengerRating)

        mockMvc.perform(
            get("${TestEntities.PASSENGER_RATING_BASE_URL}/{id}", passengerRating.passenger?.id).with(
                SecurityMockMvcRequestPostProcessors.jwt()
                    .authorities(SimpleGrantedAuthority("ROLE_admin"))
            )
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.passengerRating").value(TestEntities.PASSENGER_RATING))
    }

    @Test
    fun `test save passenger rating`() {
        val passenger = TestEntities.getPassengerForIT()
        passengerRepository.save(passenger)

        val request = TestEntities.getPassengerRatingRequestForIT(passenger.id)
        val jsonRequest = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            post(TestEntities.PASSENGER_RATING_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest).with(
                    SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(SimpleGrantedAuthority("ROLE_driver"))
                )
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

        val passengerRatingList = passengerRatingRepository.findAll()
        assertThat(passengerRatingList[0].rating).isEqualTo(TestEntities.PASSENGER_RATING)
        assertThat(passengerRatingList[0].passenger?.email).isEqualTo(TestEntities.PASSENGER_EMAIL)
    }
}