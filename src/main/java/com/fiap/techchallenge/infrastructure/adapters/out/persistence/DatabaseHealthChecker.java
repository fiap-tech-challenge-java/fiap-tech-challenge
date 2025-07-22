package com.fiap.techchallenge.infrastructure.adapters.out.persistence;

import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class DatabaseHealthChecker {

    private final DataSource dataSource;

    public DatabaseHealthChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isDatabaseUp() {
        try (Connection connection = dataSource.getConnection()) {
            return !connection.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
