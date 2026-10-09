package com.caloriebot.userservice.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;

public abstract class BaseIntegrationTest {
    protected static final Long TG_ID = 1L;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ServiceConnection
    protected static final PostgreSQLContainer POSTGRES_SQL_CONTAINER = new PostgreSQLContainer("postgres:18-alpine3.24");

    static {
        POSTGRES_SQL_CONTAINER.start();
    }

    @BeforeEach
    protected void cleanDatabase() {
        List<String> tables = jdbcTemplate.queryForList(
                "SELECT tablename FROM pg_tables " +
                        "WHERE schemaname = 'public' and tablename NOT IN ('databasechangelog', 'databasechangeloglock')",
                String.class
        );

        if (tables.isEmpty()) {
            return;
        }

        String tablesString = String.join(", ", tables);

        String truncateSql = "TRUNCATE TABLE " + tablesString + " CASCADE";
        jdbcTemplate.execute(truncateSql);
    }
}
