package com.software.modsen.driverservice.integration;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainerSetup {
    private static PostgreSQLContainer<?> postgreSQLContainer;

    @BeforeAll
    public static void setUpPostgresDB() {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("test_db")
                .withUsername("postgres")
                .withPassword("root");
        postgreSQLContainer.start();

        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }
}
