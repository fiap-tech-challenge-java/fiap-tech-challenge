package com.fiap.techchallenge.application.services;

import com.fiap.techchallenge.domain.HealthStatus;
import com.fiap.techchallenge.domain.exceptions.BusinessException;
import com.fiap.techchallenge.infrastructure.database.DatabaseHealthChecker;
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
                logger.error("Database não está respondendo");
                throw new BusinessException("Database não está disponível", "DATABASE_DOWN");
            }

            return new HealthStatus("UP", version, "OK");

        } catch (Exception ex) {
            logger.error("Erro ao verificar saúde da aplicação: {}", ex.getMessage(), ex);
            throw new BusinessException("Erro ao verificar saúde da aplicação", "HEALTH_CHECK_ERROR", ex);
        }
    }

    public HealthStatus checkHealthWithParameter(String checkType) {
        if (checkType == null || checkType.trim().isEmpty()) {
            throw new BusinessException("Tipo de verificação não pode ser vazio", "INVALID_CHECK_TYPE");
        }

        if (!checkType.equals("full") && !checkType.equals("basic")) {
            throw new BusinessException("Tipo de verificação deve ser 'full' ou 'basic'", "INVALID_CHECK_TYPE");
        }

        return checkHealth();
    }
}
