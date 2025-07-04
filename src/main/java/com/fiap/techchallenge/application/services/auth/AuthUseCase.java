package com.fiap.techchallenge.application.services.auth;

import com.fiap.techchallenge.model.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthUseCase {

    ResponseEntity<String> authenticate(LoginRequest loginRequest);
}
