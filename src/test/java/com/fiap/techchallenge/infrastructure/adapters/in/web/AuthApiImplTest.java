package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.services.auth.impl.AuthUseCaseImpl;
import com.fiap.techchallenge.model.LoginRequest;
import com.fiap.techchallenge.model.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthApiImplTest {
    @Mock
    private AuthUseCaseImpl authUseCaseImpl;

    @InjectMocks
    private AuthApiImpl authApiImpl;

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setLogin("user");
        loginRequest.setPassword("pass");
        loginResponse = new LoginResponse();
        loginResponse.setToken("jwt-token");
    }

    @Test
    void shouldLoginSuccessfully() {
        // Arrange
        when(authUseCaseImpl.authenticate(loginRequest)).thenReturn(ResponseEntity.ok(loginResponse));
        // Act
        ResponseEntity<LoginResponse> response = authApiImpl.login(loginRequest);
        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(loginResponse, response.getBody());
        verify(authUseCaseImpl).authenticate(loginRequest);
    }

    @Test
    void shouldReturnErrorWhenAuthenticationFails() {
        // Arrange
        when(authUseCaseImpl.authenticate(loginRequest)).thenReturn(ResponseEntity.status(401).build());
        // Act
        ResponseEntity<LoginResponse> response = authApiImpl.login(loginRequest);
        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(authUseCaseImpl).authenticate(loginRequest);
    }
}

