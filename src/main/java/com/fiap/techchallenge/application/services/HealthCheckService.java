package com.fiap.techchallenge.application.services;

import com.fiap.techchallenge.domain.model.HealthStatus;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.DatabaseHealthChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    private final DatabaseHealthChecker databaseHealthChecker;
    private final String version;

    public HealthCheckService(DatabaseHealthChecker databaseHealthChecker, @Value("${app.version}") String version) {
        this.databaseHealthChecker = databaseHealthChecker;
        this.version = version;
    }

    public HealthStatus checkHealth() {
        boolean dbOk = databaseHealthChecker.isDatabaseUp();
        return new HealthStatus("UP", version, dbOk ? "OK" : "ERROR");
    }

}
