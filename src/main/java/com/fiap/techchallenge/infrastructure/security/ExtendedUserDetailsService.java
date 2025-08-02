package com.fiap.techchallenge.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.UUID;

public interface ExtendedUserDetailsService extends UserDetailsService {
    UserDetails loadUserById(UUID userId);
}

