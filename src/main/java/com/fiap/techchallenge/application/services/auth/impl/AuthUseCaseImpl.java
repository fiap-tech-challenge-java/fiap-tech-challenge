package com.fiap.techchallenge.application.services.auth.impl;

import com.fiap.techchallenge.application.services.auth.AuthUseCase;
import com.fiap.techchallenge.domain.exceptions.AuthenticationException;
import com.fiap.techchallenge.infrastructure.config.JwtUtil;
import com.fiap.techchallenge.model.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUseCaseImpl implements AuthUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthUseCaseImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                           UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<String> authenticate(LoginRequest loginRequest) {
        try {
            // Busca o usuário pelo login
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getLogin());

            // Verifica se a senha está correta
            if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                throw new AuthenticationException("Credenciais inválidas");
            }

            // Gera o token JWT
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok().body(token);
        } catch (UsernameNotFoundException e) {
            throw new AuthenticationException("Usuário não encontrado");
        }
    }
}
