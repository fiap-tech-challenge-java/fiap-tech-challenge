package com.fiap.techchallenge.application.services;

import com.fiap.techchallenge.domain.model.HealthStatus;
import com.fiap.techchallenge.infrastructure.adapters.out.persistence.DatabaseHealthChecker;
import com.fiap.techchallenge.domain.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);

    private final DatabaseHealthChecker databaseHealthChecker;
    private final String version;

    public HealthCheckService(DatabaseHealthChecker databaseHealthChecker, @Value("${app.version}") String version) {
        this.databaseHealthChecker = databaseHealthChecker;
        this.version = version;
    }

    public HealthStatus checkHealth() {
        try {
            boolean dbOk = databaseHealthChecker.isDatabaseUp();

            if (!dbOk) {
                logger.error("Database is not responding.");
                throw new BusinessException("Database is not available.", "DATABASE_DOWN");
            }

            return new HealthStatus("UP", version, "OK");

        } catch (Exception ex) {
            logger.error("Error while checking application health: {}", ex.getMessage(), ex);
            throw new BusinessException("Error while checking application health.", "HEALTH_CHECK_ERROR", ex);
        }
    }

    public HealthStatus checkHealthWithParameter(String checkType) {
        if (checkType == null || checkType.trim().isEmpty()) {
            throw new BusinessException("Check type cannot be empty.", "INVALID_CHECK_TYPE");
        }

        if (!checkType.equals("full") && !checkType.equals("basic")) {
            throw new BusinessException("Check type must be 'full' or 'basic'.", "INVALID_CHECK_TYPE");
        }

        return checkHealth();
    }
}
