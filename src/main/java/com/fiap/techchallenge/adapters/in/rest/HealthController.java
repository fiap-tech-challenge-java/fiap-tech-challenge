package com.fiap.techchallenge.adapters.in.rest;


import com.fiap.techchallenge.application.HealthCheckService;
import com.fiap.techchallenge.domain.HealthStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final HealthCheckService healthCheckService;

    public HealthController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping
    public HealthStatus check() {
        return healthCheckService.checkHealth();
    }

}
