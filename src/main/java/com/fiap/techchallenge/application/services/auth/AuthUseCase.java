package com.fiap.techchallenge.application.services.auth;

import com.fiap.techchallenge.model.LoginRequest;
import com.fiap.techchallenge.model.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthUseCase {

    ResponseEntity<LoginResponse> authenticate(LoginRequest loginRequest);
}
