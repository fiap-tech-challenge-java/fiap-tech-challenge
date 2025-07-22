package com.fiap.techchallenge.application.services.auth.impl;

import com.fiap.techchallenge.application.services.auth.AuthUseCase;
import com.fiap.techchallenge.domain.exceptions.CustomAuthenticationException;
import com.fiap.techchallenge.infrastructure.config.JwtUtil;
import com.fiap.techchallenge.infrastructure.config.UserDetailsImpl;
import com.fiap.techchallenge.model.LoginRequest;
import com.fiap.techchallenge.model.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class AuthUseCaseImpl implements AuthUseCase {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthUseCaseImpl(JwtUtil jwtUtil, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<LoginResponse> authenticate(LoginRequest loginRequest) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getLogin());

            if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                throw new CustomAuthenticationException("Credenciais inválidas");
            }

            UUID userId = ((UserDetailsImpl) userDetails).getId();
            String role = userDetails.getAuthorities().iterator().next().getAuthority();

            String token = jwtUtil.generateToken(userId, role);

            long expiration = jwtUtil.extractAllClaims(token).getExpiration().getTime();
            long now = System.currentTimeMillis();
            OffsetDateTime expiresAt = OffsetDateTime.ofInstant(Instant.ofEpochMilli(expiration),
                    ZoneId.systemDefault());
            int expiresIn = (int) ((expiration - now) / 1000);

            LoginResponse response = new LoginResponse(token, null, null, expiresAt, expiresIn, userId);

            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            throw new CustomAuthenticationException("Usuário não encontrado");
        }
    }
}
