package com.software.modsen.passengerservice.integration

import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.PostgreSQLContainer

open class PostgresTestContainerSetup {
    companion object {
        private lateinit var postgreSQLContainer: PostgreSQLContainer<*>

        @JvmStatic
        @BeforeAll
        fun setUpPostgresDB() {
            postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:15-alpine").apply {
                withDatabaseName("test_db")
                withUsername("postgres")
                withPassword("root")
            }
            postgreSQLContainer.start()

            System.setProperty("spring.datasource.url", postgreSQLContainer.jdbcUrl)
            System.setProperty("spring.datasource.username", postgreSQLContainer.username)
            System.setProperty("spring.datasource.password", postgreSQLContainer.password)
        }
    }
}