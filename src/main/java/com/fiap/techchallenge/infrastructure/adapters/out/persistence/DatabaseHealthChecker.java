package com.fiap.techchallenge.infrastructure.adapters.out.persistence;

import org.springframework.boot.actuate.health.Health; // Import required for HealthIndicator
import org.springframework.boot.actuate.health.HealthIndicator; // Import required for HealthIndicator
import org.springframework.stereotype.Component; // Use @Component for HealthIndicators
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException; // Import SQLException

@Component
public class DatabaseHealthChecker implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                return Health.up()
                             .withDetail("database", "Connection OK")
                             .withDetail("url", connection.getMetaData().getURL())
                             .build();
            } else {
                return Health.down()
                             .withDetail("database", "Connection NOT valid")
                             .build();
            }
        } catch (SQLException e) {
            return Health.down(e)
                         .withDetail("database", "Connection error")
                         .withDetail("message", e.getMessage())
                         .build();
        }
    }

    public boolean isDatabaseUp() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(1);
        } catch (Exception e) {
            return false;
        }
    }
}