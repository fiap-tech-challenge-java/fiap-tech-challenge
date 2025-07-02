package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.services.HealthCheckService;
import com.fiap.techchallenge.domain.exceptions.ResourceNotFoundException;
import com.fiap.techchallenge.domain.model.HealthStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{checkType}")
    public HealthStatus checkWithType(@PathVariable String checkType) {
        return healthCheckService.checkHealthWithParameter(checkType);
    }

    @GetMapping("/test-exception")
    public HealthStatus testException(@RequestParam(required = false) String type) {
        if ("not-found".equals(type)) {
            throw new ResourceNotFoundException("Recurso de teste", "tipo", type);
        }
        if ("business".equals(type)) {
            throw new RuntimeException("Erro de negócio simulado");
        }
        return healthCheckService.checkHealth();
    }
}
