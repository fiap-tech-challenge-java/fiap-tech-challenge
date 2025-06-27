package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.services.HealthCheckService;
import com.fiap.techchallenge.domain.model.HealthStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthApiImpl {

    private final HealthCheckService healthCheckService;

    public HealthApiImpl(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping
    public HealthStatus check() {
        return healthCheckService.checkHealth();
    }

}
