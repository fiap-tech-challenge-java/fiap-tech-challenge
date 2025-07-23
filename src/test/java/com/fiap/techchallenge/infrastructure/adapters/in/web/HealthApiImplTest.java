package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.services.HealthCheckService;
import com.fiap.techchallenge.domain.exceptions.ResourceNotFoundException;
import com.fiap.techchallenge.domain.model.HealthStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HealthApiImplTest {
    @Mock
    private HealthCheckService healthCheckService;

    @InjectMocks
    private HealthApiImpl healthApiImpl;

    private HealthStatus healthStatus;

    @BeforeEach
    void setUp() {
        healthStatus = new HealthStatus(
            "true", // isHealthy
            "All systems operational", // message
            null // details
        );
        // ...set fields as needed
    }

    @Test
    void shouldReturnHealthStatusSuccessfully() {
        // Arrange
        when(healthCheckService.checkHealth()).thenReturn(healthStatus);
        // Act
        HealthStatus result = healthApiImpl.check();
        // Assert
        assertEquals(healthStatus, result);
        verify(healthCheckService).checkHealth();
    }

    @Test
    void shouldReturnHealthStatusWithTypeSuccessfully() {
        // Arrange
        when(healthCheckService.checkHealthWithParameter("db")).thenReturn(healthStatus);
        // Act
        HealthStatus result = healthApiImpl.checkWithType("db");
        // Assert
        assertEquals(healthStatus, result);
        verify(healthCheckService).checkHealthWithParameter("db");
    }

    @Test
    void shouldThrowResourceNotFoundExceptionForTestExceptionEndpoint() {
        // Act & Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> healthApiImpl.testException("not-found"));
        assertEquals("Test resource", ex.getResourceName());
    }

    @Test
    void shouldThrowBusinessExceptionForTestExceptionEndpoint() {
        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> healthApiImpl.testException("business"));
        assertEquals("Simulated business error", ex.getMessage());
    }

    @Test
    void shouldReturnHealthStatusForTestExceptionEndpointWithNoType() {
        // Arrange
        when(healthCheckService.checkHealth()).thenReturn(healthStatus);
        // Act
        HealthStatus result = healthApiImpl.testException(null);
        // Assert
        assertEquals(healthStatus, result);
        verify(healthCheckService).checkHealth();
    }
}

