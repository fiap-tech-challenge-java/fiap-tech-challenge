package com.fiap.techchallenge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
public class DatabaseTestRunner {

    private final DataSource dataSource;

    public DatabaseTestRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Test
    void shouldConnectToDatabase() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertFalse(connection.isClosed());
        }
    }
}
