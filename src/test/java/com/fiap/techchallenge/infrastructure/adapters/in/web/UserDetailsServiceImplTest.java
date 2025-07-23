package com.fiap.techchallenge.infrastructure.adapters.in.web;

import com.fiap.techchallenge.application.ports.in.user.dtos.User;
import com.fiap.techchallenge.application.ports.out.user.UserRepository;
import com.fiap.techchallenge.infrastructure.config.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setLogin("testuser");
        user.setPassword("password");
        // ...set other fields as needed
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByUsername() {
        // Arrange
        when(userRepository.findByLogin("testuser")).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("testuser"));
        verify(userRepository).findByLogin("testuser");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        // Arrange
        when(userRepository.findByIdOnly(userId)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserById(userId));
        verify(userRepository).findByIdOnly(userId);
    }
}

