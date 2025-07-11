package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.api.AuthApi;
import com.fiap.techchallenge.application.services.auth.impl.AuthUseCaseImpl;
import com.fiap.techchallenge.model.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthApiImpl implements AuthApi {

    private final AuthUseCaseImpl authUseCaseImpl;

    public AuthApiImpl(AuthUseCaseImpl authUseCaseImpl) {
        this.authUseCaseImpl = authUseCaseImpl;
    }

    @Override
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return authUseCaseImpl.authenticate(loginRequest);
    }
}
