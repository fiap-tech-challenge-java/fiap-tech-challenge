package com.fiap.techchallenge.infrastructure.adapters.out.persistence;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


import javax.sql.DataSource;
import java.sql.Connection;


@Configuration
@Profile("dev")                // executa só no perfil dev
public class DatabaseHealthChecker implements CommandLineRunner {

    private final DataSource dataSource;

    public DatabaseHealthChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("\u001B[32m\uD83D\uDE80  🎉 Conexão bem-sucedida! URL: \u001B[0m" +
                    conn.getMetaData().getURL());
        } catch (Exception e) {
            System.err.println("❌ Falha ao conectar: " + e.getMessage());
            throw e;
        }
    }

    public boolean isDatabaseUp() {
        try (Connection connection = dataSource.getConnection()) {
            return !connection.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
